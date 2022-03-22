/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.resource.transaction.internal;

import java.util.Map;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.boot.registry.selector.spi.StrategySelector;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.internal.log.DeprecationLogger;
import org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorBuilderImpl;
import org.hibernate.resource.transaction.spi.TransactionCoordinatorBuilder;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * StandardServiceInitiator for initiating the TransactionCoordinatorBuilder service.
 *
 * @author Andrea Boriero
 * @author Steve Ebersole
 */
public class TransactionCoordinatorBuilderInitiator implements StandardServiceInitiator<TransactionCoordinatorBuilder> {
	public static final String LEGACY_SETTING_NAME = "hibernate.transaction.factory_class";

	/**
	 * Singleton access
	 */
	public static final TransactionCoordinatorBuilderInitiator INSTANCE = new TransactionCoordinatorBuilderInitiator();

	@Override
	public TransactionCoordinatorBuilder initiateService(Map<String, Object> configurationValues, ServiceRegistryImplementor registry) {
		// 选择事务协调策略 ...
		return registry.getService( StrategySelector.class ).resolveDefaultableStrategy(
				TransactionCoordinatorBuilder.class, // 一种事务协调器构建器策略 ...
				determineStrategySelection( configurationValues ), // 根据 策略 然后获取对应的Builder ..
				JdbcResourceLocalTransactionCoordinatorBuilderImpl.INSTANCE // 默认值
		);
	}

	private static Object determineStrategySelection(Map configurationValues) {
		// 根据策略属性设置获取
		final Object coordinatorStrategy = configurationValues.get( AvailableSettings.TRANSACTION_COORDINATOR_STRATEGY );
		if ( coordinatorStrategy != null ) {
			return coordinatorStrategy;
		}
		//  遗留
		final Object legacySetting = configurationValues.get( LEGACY_SETTING_NAME );
		if ( legacySetting != null ) {
			// 日志纪录
			DeprecationLogger.DEPRECATION_LOGGER.logDeprecatedTransactionFactorySetting(
					LEGACY_SETTING_NAME,
					AvailableSettings.TRANSACTION_COORDINATOR_STRATEGY
			);
			return legacySetting;
		}

		// triggers the default
		return null;
	}

	@Override
	public Class<TransactionCoordinatorBuilder> getServiceInitiated() {
		return TransactionCoordinatorBuilder.class;
	}
}
