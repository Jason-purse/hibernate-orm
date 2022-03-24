/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.metamodel.internal;

import java.util.Map;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.internal.util.config.ConfigurationHelper;

/**
 * Enumerated setting used to control whether Hibernate looks for and populates
 * JPA static metamodel models of application's domain model.
 * 这里列出的配置被用来控制 Hibernate 是否应该查询并收集应用程序域模型的 JPA 静态元模型模型。
 * @author Andrea Boriero
 */
public enum JpaStaticMetaModelPopulationSetting {
	/**
	 * ENABLED indicates that Hibernate will look for the JPA static metamodel description
	 * of the application domain model and populate it.
	 */
	ENABLED,
	/**
	 * DISABLED indicates that Hibernate will not look for the JPA static metamodel description
	 * of the application domain model.
	 */
	DISABLED,
	/** 会忽略非JPA 特性 - (这些特性可能会导致收集失败)
	 * SKIP_UNSUPPORTED works as ENABLED but ignores any non-JPA features that would otherwise
	 * result in the population failing.
	 */
	SKIP_UNSUPPORTED;

	public static JpaStaticMetaModelPopulationSetting parse(String setting) {
		if ( "enabled".equalsIgnoreCase( setting ) ) {
			return ENABLED;
		}
		else if ( "disabled".equalsIgnoreCase( setting ) ) {
			return DISABLED;
		}
		else {
			return SKIP_UNSUPPORTED;
		}
	}

	public static JpaStaticMetaModelPopulationSetting determineJpaStaticMetaModelPopulationSetting(Map configurationValues) {
		return parse( determineSetting( configurationValues ) );
	}

	private static String determineSetting(Map configurationValues) {
		final String setting = ConfigurationHelper.getString(
				AvailableSettings.STATIC_METAMODEL_POPULATION,
				configurationValues
		);
		if ( setting != null ) {
			return setting;
		}

		return null;
	}
}
