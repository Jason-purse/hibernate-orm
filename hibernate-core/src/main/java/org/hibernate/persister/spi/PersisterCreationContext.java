/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.persister.spi;

import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.RuntimeModelCreationContext;
import org.hibernate.resource.beans.spi.ManagedBeanRegistry;
import org.hibernate.type.descriptor.java.spi.JavaTypeRegistry;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * "Parameter object" providing access to additional information that may be needed
 * in the creation of the persisters.
 *	参数对象 - 提供访问额外的信息(也许在持久化器创建的过程中这是需要的)
 * @author Steve Ebersole
 *
 * @deprecated Use {@link RuntimeModelCreationContext} instead
 */
@Deprecated
public interface PersisterCreationContext {
	SessionFactoryImplementor getSessionFactory();

	BootstrapContext getBootstrapContext();

	default TypeConfiguration getTypeConfiguration() {
		return getBootstrapContext().getTypeConfiguration();
	}

	MetadataImplementor getMetadata();

	default ManagedBeanRegistry getManagedBeanRegistry() {
		return getSessionFactory().getServiceRegistry().getService( ManagedBeanRegistry.class );
	}

	default JavaTypeRegistry getJavaTypeRegistry() {
		return getTypeConfiguration().getJavaTypeRegistry();
	}
}
