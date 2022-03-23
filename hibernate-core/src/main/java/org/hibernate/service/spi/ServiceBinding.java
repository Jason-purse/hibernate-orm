/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service.spi;

import org.hibernate.service.Service;

import org.jboss.logging.Logger;

/**
 * Models a binding for a particular service
 *
 * 对于一个特殊服务绑定的模型
 *
 * @author Steve Ebersole
 */
public final class ServiceBinding<R extends Service> {
	private static final Logger log = Logger.getLogger( ServiceBinding.class );

	public interface ServiceLifecycleOwner {

		<R extends Service> R initiateService(ServiceInitiator<R> serviceInitiator);

		<R extends Service> void configureService(ServiceBinding<R> binding);
		<R extends Service> void injectDependencies(ServiceBinding<R> binding);
		<R extends Service> void startService(ServiceBinding<R> binding);

		<R extends Service> void stopService(ServiceBinding<R> binding);
	}

	// 服务生命周期拥有者
	private final ServiceLifecycleOwner lifecycleOwner;
	// 服务角色
	private final Class<R> serviceRole;
	// 服务初始化器
	private final ServiceInitiator<R> serviceInitiator;
	// 服务 volatile
	private volatile R service;

	public ServiceBinding(ServiceLifecycleOwner lifecycleOwner, Class<R> serviceRole, R service) {
		this.lifecycleOwner = lifecycleOwner;
		this.serviceRole = serviceRole;
		this.serviceInitiator = null;
		this.service = service;
	}

	public ServiceBinding(ServiceLifecycleOwner lifecycleOwner, ServiceInitiator<R> serviceInitiator) {
		this.lifecycleOwner = lifecycleOwner;
		this.serviceRole = serviceInitiator.getServiceInitiated();
		this.serviceInitiator = serviceInitiator;
	}

	public ServiceLifecycleOwner getLifecycleOwner() {
		return lifecycleOwner;
	}

	public Class<R> getServiceRole() {
		return serviceRole;
	}

	public ServiceInitiator<R> getServiceInitiator() {
		return serviceInitiator;
	}

	public R getService() {
		return service;
	}

	public void setService(R service) {
		if ( this.service != null ) {
			if ( log.isDebugEnabled() ) {
				log.debug( "Overriding existing service binding [" + serviceRole.getName() + "]" );
			}
		}
		this.service = service;
	}
}
