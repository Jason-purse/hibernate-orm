/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.dialect.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.boot.registry.selector.spi.StrategySelectionException;
import org.hibernate.boot.registry.selector.spi.StrategySelector;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectFactory;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfoSource;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.internal.CoreLogging;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import static org.hibernate.internal.log.DeprecationLogger.DEPRECATION_LOGGER;

/**
 * Standard implementation of the {@link DialectFactory} service.
 *
 * @author Steve Ebersole
 */
public class DialectFactoryImpl implements DialectFactory, ServiceRegistryAwareService {
	private static final CoreMessageLogger LOG = CoreLogging.messageLogger( "SQL dialect" );

	private StrategySelector strategySelector; // 策略选择器
	private DialectResolver dialectResolver; // 方言解析器

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		this.strategySelector = serviceRegistry.getService( StrategySelector.class );
		this.dialectResolver = serviceRegistry.getService( DialectResolver.class );
	}

	/**
	 * Intended only for use from testing.
	 *
	 * @param dialectResolver The DialectResolver to use
	 */
	public void setDialectResolver(DialectResolver dialectResolver) {
		this.dialectResolver = dialectResolver;
	}

	@Override
	public Dialect buildDialect(Map<String,Object> configValues, DialectResolutionInfoSource resolutionInfoSource) throws HibernateException {
		// 获取有没有 方言reference
		final Object dialectReference = configValues.get( AvailableSettings.DIALECT );
		// 如果不为空,直接构造
		Dialect dialect = !isEmpty( dialectReference ) ?
				constructDialect( dialectReference, resolutionInfoSource ) :
				// 否则判断
				determineDialect( resolutionInfoSource );
		// 日志纪录
		logSelectedDialect( dialect );
		return dialect;
	}

	private static void logSelectedDialect(Dialect dialect) {
		LOG.usingDialect( dialect );

		Class<? extends Dialect> dialectClass = dialect.getClass();
		if ( dialectClass.isAnnotationPresent( Deprecated.class ) ) {
			Class<?> superDialectClass = dialectClass.getSuperclass();
			if ( !superDialectClass.isAnnotationPresent( Deprecated.class )
					&& !superDialectClass.equals( Dialect.class ) ) {
				DEPRECATION_LOGGER.deprecatedDialect( dialectClass.getSimpleName(), superDialectClass.getName() );
			}
			else {
				DEPRECATION_LOGGER.deprecatedDialect( dialectClass.getSimpleName() );
			}
		}
	}

	@SuppressWarnings("SimplifiableIfStatement")
	private boolean isEmpty(Object dialectReference) {
		if ( dialectReference != null ) {
			// the referenced value is not null
			if ( dialectReference instanceof String ) {
				// if it is a String, it might still be empty though...
				return StringHelper.isEmpty( (String) dialectReference );
			}
			return false;
		}
		return true;
	}

	private Dialect constructDialect(Object dialectReference, DialectResolutionInfoSource resolutionInfoSource) {
		try {
			// 通过策略解析器  解析一种策略  以及一个唯一的名称  确定一种信息
			Dialect dialect = strategySelector.resolveStrategy(
					Dialect.class,
					dialectReference,
					(Dialect) null, // 默认值
					// 策略创建器
					(dialectClass) -> {
						try {
							try {
								// 如果不为空
								if (resolutionInfoSource != null) {
									// 获取构建器(这是一种约定)
									return dialectClass.getConstructor( DialectResolutionInfo.class ).newInstance(
											resolutionInfoSource.getDialectResolutionInfo()
									);
								}
							}
							catch (NoSuchMethodException nsme) {
								// pass
								// 没有这种约定
							}
							// 直接new 一个实例
							return dialectClass.newInstance();
						}
						catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
							// 否则表示这方言无法实例化
							throw new StrategySelectionException(
									String.format( "Could not instantiate named dialect class [%s]", dialectClass.getName() ),
									e
							);
						}
					}
			);
			if ( dialect == null ) {
				throw new HibernateException( "Unable to construct requested dialect [" + dialectReference + "]" );
			}
			return dialect;
		}
		catch (HibernateException e) {
			throw e;
		}
		catch (Exception e) {
			throw new HibernateException( "Unable to construct requested dialect [" + dialectReference + "]", e );
		}
	}

	/**
	 * Determine the appropriate Dialect to use given the connection.
	 *
	 * @param resolutionInfoSource Access to DialectResolutionInfo used to resolve the Dialect.
	 *
	 * @return The appropriate dialect instance.
	 *
	 * @throws HibernateException No connection given or no resolver could make
	 * the determination from the given connection.
	 */
	private Dialect determineDialect(DialectResolutionInfoSource resolutionInfoSource) {
		// 如果要解析 必须要提供JDBC metadata,否则必须 提供
		if ( resolutionInfoSource == null ) {
			throw new HibernateException(
					"Unable to determine Dialect without JDBC metadata "
					+ "(please set 'javax.persistence.jdbc.url', 'hibernate.connection.url', or 'hibernate.dialect')"
			);
		}

		final DialectResolutionInfo info = resolutionInfoSource.getDialectResolutionInfo();
		// 解析方言
		final Dialect dialect = dialectResolver.resolveDialect( info );

		if ( dialect == null ) {
			throw new HibernateException(
					"Unable to determine Dialect for " + info.getDatabaseName() + " "
					+ info.getDatabaseMajorVersion() + "." + info.getDatabaseMinorVersion()
					+ " (please set 'hibernate.dialect' or register a Dialect resolver)"
			);
		}

		return dialect;
	}
}
