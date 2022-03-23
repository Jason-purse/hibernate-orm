/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.spi;

import org.hibernate.boot.model.TypeDefinitionRegistry;
import org.hibernate.boot.model.naming.ObjectNameNormalizer;
import org.hibernate.internal.util.config.ConfigurationHelper;

/**
 * Describes the context in which the process of building Metadata out of MetadataSources occurs.
 * 描述 构建元数据的元数据来源出现的过程 ...
 *
 * BindingContext are generally hierarchical getting more specific as we "go
 * down".  E.g.  global -> PU -> document -> mapping
 *
 * BindingContext 通常是一个体系 通常可以向下了解到更多
 *
 * @author Steve Ebersole
 *
 * @since 5.0
 */
public interface MetadataBuildingContext {
	BootstrapContext getBootstrapContext();

	/**
	 * Access to the options specified by the {@link org.hibernate.boot.MetadataBuilder}
	 *
	 * @return The options
	 */
	MetadataBuildingOptions getBuildingOptions();

	/**
	 * Access to mapping defaults in effect for this context
	 *
	 * @return The mapping defaults.
	 */
	MappingDefaults getMappingDefaults();

	/**
	 * Access to the collector of metadata as we build it.
	 *
	 * @return The metadata collector.
	 */
	InFlightMetadataCollector getMetadataCollector();

	/**
	 * Not sure how I feel about this exposed here
	 *
	 * @return The ObjectNameNormalizer
	 */
	ObjectNameNormalizer getObjectNameNormalizer();

	default int getPreferredSqlTypeCodeForBoolean() {
		return ConfigurationHelper.getPreferredSqlTypeCodeForBoolean( getBootstrapContext().getServiceRegistry() );
	}

	TypeDefinitionRegistry getTypeDefinitionRegistry();

	/**
	 * The name of the contributor whose mappings we are currently processing
	 */
	String getCurrentContributorName();
}
