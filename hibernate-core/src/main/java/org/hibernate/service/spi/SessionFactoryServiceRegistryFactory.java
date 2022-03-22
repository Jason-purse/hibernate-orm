/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service.spi;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.Service;

/**
 * SessionFactoryServiceRegistry  实例的构建器约定
 * Contract for builder of {@link SessionFactoryServiceRegistry} instances.
 * <p/>
 * Is itself a service within the standard service registry.
 *
 * 在标准服务注册表里 他本是是一个服务 ...
 *
 * @author Steve Ebersole
 */
public interface SessionFactoryServiceRegistryFactory extends Service {
	/**
	 * Create the registry.
	 *
	 *
	 * @param sessionFactory The (still being built) session factory.  Generally this is useful
	 * for grabbing a reference for later use.
	 *
	 *
	 *                          However, care should be taken when invoking on
	 * the session factory until after it has been fully initialized.
	 *
	 *                       应该变得小心,当会话工厂完全初始化之后在调用 ... 相关的方法 收集引用来后续使用...
	 * @param sessionFactoryOptions The build options.
	 *
	 * @return The registry
	 */
	SessionFactoryServiceRegistry buildServiceRegistry(
			SessionFactoryImplementor sessionFactory,
			SessionFactoryOptions sessionFactoryOptions);

}
