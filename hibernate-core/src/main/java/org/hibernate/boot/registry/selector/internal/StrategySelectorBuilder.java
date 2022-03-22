/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.registry.selector.internal;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.registry.selector.SimpleStrategyRegistrationImpl;
import org.hibernate.boot.registry.selector.StrategyRegistration;
import org.hibernate.boot.registry.selector.StrategyRegistrationProvider;
import org.hibernate.boot.registry.selector.spi.DialectSelector;
import org.hibernate.boot.registry.selector.spi.StrategySelectionException;
import org.hibernate.boot.registry.selector.spi.StrategySelector;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.internal.SimpleCacheKeysFactory;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.query.sqm.mutation.internal.cte.CteMutationStrategy;
import org.hibernate.query.sqm.mutation.internal.temptable.GlobalTemporaryTableMutationStrategy;
import org.hibernate.query.sqm.mutation.internal.temptable.LocalTemporaryTableMutationStrategy;
import org.hibernate.query.sqm.mutation.internal.temptable.PersistentTableMutationStrategy;
import org.hibernate.query.sqm.mutation.spi.SqmMultiTableMutationStrategy;
import org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorBuilderImpl;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionCoordinatorBuilderImpl;
import org.hibernate.resource.transaction.spi.TransactionCoordinatorBuilder;
import org.hibernate.type.FormatMapper;
import org.hibernate.type.JacksonJsonFormatMapper;
import org.hibernate.type.JsonBJsonFormatMapper;

import org.jboss.logging.Logger;

/**
 * Builder for {@link StrategySelector} instances.
 *
 * @author Steve Ebersole
 */
public class StrategySelectorBuilder {
	private static final Logger log = Logger.getLogger( StrategySelectorBuilder.class );

	private final List<StrategyRegistration<?>> explicitStrategyRegistrations = new ArrayList<>();

	/**
	 * Adds an explicit (as opposed to discovered) strategy registration.
	 *
	 * @param strategy The strategy
	 * @param implementation The strategy implementation
	 * @param name The registered name
	 * @param <T> The type of the strategy.  Used to make sure that the strategy and implementation are type
	 * compatible.
	 */
	public <T> void addExplicitStrategyRegistration(Class<T> strategy, Class<? extends T> implementation, String name) {
		addExplicitStrategyRegistration( new SimpleStrategyRegistrationImpl<>( strategy, implementation, name ) );
	}

	/**
	 * Adds an explicit (as opposed to discovered) strategy registration.
	 *
	 * @param strategyRegistration The strategy implementation registration.
	 * @param <T> The type of the strategy.  Used to make sure that the strategy and implementation are type
	 * compatible.
	 */
	public <T> void addExplicitStrategyRegistration(StrategyRegistration<T> strategyRegistration) {
		if ( !strategyRegistration.getStrategyRole().isInterface() ) {
			// not good form...
			if ( log.isDebugEnabled() ) {
				log.debugf( "Registering non-interface strategy : %s", strategyRegistration.getStrategyRole().getName() );
			}
		}

		if ( ! strategyRegistration.getStrategyRole().isAssignableFrom( strategyRegistration.getStrategyImplementation() ) ) {
			throw new StrategySelectionException(
					"Implementation class [" + strategyRegistration.getStrategyImplementation().getName()
							+ "] does not implement strategy interface ["
							+ strategyRegistration.getStrategyRole().getName() + "]"
			);
		}
		explicitStrategyRegistrations.add( strategyRegistration );
	}

	/**
	 * Builds the selector.
	 *
	 * @param classLoaderService The class loading service used to (attempt to) resolve any un-registered
	 * strategy implementations.
	 *
	 * @return The selector.
	 */
	public StrategySelector buildSelector(ClassLoaderService classLoaderService) {
		// 构建选择器
		// 类加载器服务 用来解析任何一个未注册的策略实现 ....
		final StrategySelectorImpl strategySelector = new StrategySelectorImpl( classLoaderService );

		// build the baseline...
		// 懒惰的注册策略服务
		// 将Dialect selector 注册进来,作为选择方言的策略
		strategySelector.registerStrategyLazily(
				Dialect.class,
				new AggregatedDialectSelector( classLoaderService.loadJavaServices( DialectSelector.class ) )
		);
		// JTA平台 策略检测
		strategySelector.registerStrategyLazily( JtaPlatform.class, new DefaultJtaPlatformSelector() );

		// 增加事务协调器构建器
		addTransactionCoordinatorBuilders( strategySelector );

		// 多表 映射策略
		addSqmMultiTableMutationStrategies( strategySelector );

		// 隐式命名策略
		addImplicitNamingStrategies( strategySelector );

		// 缓存Key 工厂
		addCacheKeysFactories( strategySelector );

		// json 格式化Mapper
		addJsonFormatMappers( strategySelector );

		// 应用自动发现的 注册集 ..
		// apply auto-discovered registrations
		// spi 加载的
		for ( StrategyRegistrationProvider provider : classLoaderService.loadJavaServices( StrategyRegistrationProvider.class ) ) {
			// 拿到这个提供器所提供的所有登记表 然后注册 ...
			for ( StrategyRegistration<?> discoveredStrategyRegistration : provider.getStrategyRegistrations() ) {
				applyFromStrategyRegistration( strategySelector, discoveredStrategyRegistration );
			}
		}

		// apply customizations
		// 应用自定义的  显式添加的 ...
		for ( StrategyRegistration<?> explicitStrategyRegistration : explicitStrategyRegistrations ) {
			applyFromStrategyRegistration( strategySelector, explicitStrategyRegistration );
		}

		return strategySelector;
	}

	private <T> void applyFromStrategyRegistration(StrategySelectorImpl strategySelector, StrategyRegistration<T> strategyRegistration) {
		// 根据这个注册表上的选择器名称 ,, 然后注册策略实现器(本质上完全可以说是 一个selector 或者协调器)
		for ( String name : strategyRegistration.getSelectorNames() ) {
			strategySelector.registerStrategyImplementor(
					strategyRegistration.getStrategyRole(),
					name,
					strategyRegistration.getStrategyImplementation()
			);
		}
	}

	private void addTransactionCoordinatorBuilders(StrategySelectorImpl strategySelector) {
		// JDBC ..
		strategySelector.registerStrategyImplementor(
				TransactionCoordinatorBuilder.class,
				JdbcResourceLocalTransactionCoordinatorBuilderImpl.SHORT_NAME,
				JdbcResourceLocalTransactionCoordinatorBuilderImpl.class
		);
		// JTA
		strategySelector.registerStrategyImplementor(
				TransactionCoordinatorBuilder.class,
				JtaTransactionCoordinatorBuilderImpl.SHORT_NAME,
				JtaTransactionCoordinatorBuilderImpl.class
		);

		// legacy JDBC
		// add the legacy TransactionFactory impl names...
		strategySelector.registerStrategyImplementor(
				TransactionCoordinatorBuilder.class,
				"org.hibernate.transaction.JDBCTransactionFactory",
				JdbcResourceLocalTransactionCoordinatorBuilderImpl.class
		);
		// jta legacy 遗留 .
		strategySelector.registerStrategyImplementor(
				TransactionCoordinatorBuilder.class,
				"org.hibernate.transaction.JTATransactionFactory",
				JtaTransactionCoordinatorBuilderImpl.class
		);
		// cmt ???
		strategySelector.registerStrategyImplementor(
				TransactionCoordinatorBuilder.class,
				"org.hibernate.transaction.CMTTransactionFactory",
				JtaTransactionCoordinatorBuilderImpl.class
		);
	}

	private void addSqmMultiTableMutationStrategies(StrategySelectorImpl strategySelector) {
		// 条件 修改策略
		strategySelector.registerStrategyImplementor(
				SqmMultiTableMutationStrategy.class,
				CteMutationStrategy.SHORT_NAME,
				CteMutationStrategy.class
		);
		// 全局临时表 策略
		strategySelector.registerStrategyImplementor(
				SqmMultiTableMutationStrategy.class,
				GlobalTemporaryTableMutationStrategy.SHORT_NAME,
				GlobalTemporaryTableMutationStrategy.class
		);
		// 策略  本地临时表策略
		strategySelector.registerStrategyImplementor(
				SqmMultiTableMutationStrategy.class,
				LocalTemporaryTableMutationStrategy.SHORT_NAME,
				LocalTemporaryTableMutationStrategy.class
		);
		//  持久化表策略
		strategySelector.registerStrategyImplementor(
				SqmMultiTableMutationStrategy.class,
				PersistentTableMutationStrategy.SHORT_NAME,
				PersistentTableMutationStrategy.class
		);
	}

	private void addImplicitNamingStrategies(StrategySelectorImpl strategySelector) {

		// prefer jpa
		// 默认
		strategySelector.registerStrategyImplementor(
				ImplicitNamingStrategy.class,
				"default",
				ImplicitNamingStrategyJpaCompliantImpl.class
		);
		// jpa
		strategySelector.registerStrategyImplementor(
				ImplicitNamingStrategy.class,
				"jpa",
				ImplicitNamingStrategyJpaCompliantImpl.class
		);
		//  legacy-jpa
		strategySelector.registerStrategyImplementor(
				ImplicitNamingStrategy.class,
				"legacy-jpa",
				ImplicitNamingStrategyLegacyJpaImpl.class
		);
		//  遗留的 hbm  hibernate-table-mapping 映射
		strategySelector.registerStrategyImplementor(
				ImplicitNamingStrategy.class,
				"legacy-hbm",
				ImplicitNamingStrategyLegacyHbmImpl.class
		);
		// 组件路径
		strategySelector.registerStrategyImplementor(
				ImplicitNamingStrategy.class,
				"component-path",
				ImplicitNamingStrategyComponentPathImpl.class
		);
	}

	private void addCacheKeysFactories(StrategySelectorImpl strategySelector) {
			// 默认
		strategySelector.registerStrategyImplementor(
			CacheKeysFactory.class,
			DefaultCacheKeysFactory.SHORT_NAME,
			DefaultCacheKeysFactory.class
		);
		// 简单Key
		strategySelector.registerStrategyImplementor(
			CacheKeysFactory.class,
			SimpleCacheKeysFactory.SHORT_NAME,
			SimpleCacheKeysFactory.class
		);
	}

	private void addJsonFormatMappers(StrategySelectorImpl strategySelector) {

		// 默认jackson
		strategySelector.registerStrategyImplementor(
				FormatMapper.class,
				JacksonJsonFormatMapper.SHORT_NAME,
				JacksonJsonFormatMapper.class
		);

		// jsonb
		strategySelector.registerStrategyImplementor(
				FormatMapper.class,
				JsonBJsonFormatMapper.SHORT_NAME,
				JsonBJsonFormatMapper.class
		);
	}
}
