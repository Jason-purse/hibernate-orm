/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service.spi;

import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;

/**
 * Additional integration contracts for a service registry.
 *
 * 一个服务注册机的额外的集成约定 ..
 *
 * 它可以包含一些额外的集成约定
 *
 * @author Steve Ebersole
 */
public interface ServiceRegistryImplementor extends ServiceRegistry {
	/**
	 * Locate the binding for the given role.  Should, generally speaking, look into parent registry if one.
	 * 针对给定的role 分配 （定位）一个binding ,应该通常来说,如果有的话，查询父注册表。
	 * @param serviceRole The service role for which to locate a binding.
	 * @param <R> generic return type.
	 *
	 * @return The located binding; may be {@code null}
	 */
	<R extends Service> ServiceBinding<R> locateServiceBinding(Class<R> serviceRole);

	@Override
	default void close() {
		destroy();
	}

	/**
	 * Release resources
	 * 释放资源
	 */
	void destroy();

	/**
	 * When a registry is created with a parent, the parent is notified of the child
	 * via this callback.
	 *
	 * 当一个注册机通过父亲创建,孩子给父亲发送通知靠这个..
	 */
	void registerChild(ServiceRegistryImplementor child);

	/**
	 * When a registry is created with a parent, the parent is notified of the child
	 * via this callback.
	 */
	void deRegisterChild(ServiceRegistryImplementor child);
}
