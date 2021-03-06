/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service;

/**
 * A registry of {@linkplain Service services}. This interface abstracts
 * the operations of:
 *
 * 服务的注册机  这个接口抽象 BootstrapServiceRegistry 以及  StandardServiceRegistry的抽象
 * <ul>
 * <li>{@linkplain org.hibernate.boot.registry.BootstrapServiceRegistry
 * bootstrap service registries}, which may be obtained from a
 * {@link org.hibernate.boot.registry.BootstrapServiceRegistryBuilder},
 * 一个通过BootStrapServiceRegistryBuilder
 * and
 * <li>{@linkplain org.hibernate.boot.registry.StandardServiceRegistry
 * standard service registries}, which may be obtained from a
 * {@link org.hibernate.boot.registry.StandardServiceRegistryBuilder}.
 * 一个通过标准的服务注册, 通过StandardServiceRegistryBuilder ...
 * </ul>
 *
 * @author Steve Ebersole
 */
public interface ServiceRegistry extends AutoCloseable {
	/**
	 * Retrieve this registry's parent registry.
	 * 
	 * @return The parent registry.  May be null.
	 */
	ServiceRegistry getParentServiceRegistry();

	/**
	 * Retrieve a service by role.  If service is not found, but a {@link org.hibernate.service.spi.ServiceInitiator} is
	 * registered for this service role, the service will be initialized and returned.
	 * <p/>
	 * NOTE: We cannot return {@code <R extends Service<T>>} here because the service might come from the parent...
	 *
	 * 根据角色  抓取一个服务,如果服务没有发现,  但是发现了ServiceInitiator 那么  将会初始化并返回 ...
	 * 其实就是基于 Class 抓取 服务
	 * 
	 * @param serviceRole The service role
	 * @param <R> The service role type
	 *
	 * @return The requested service or null if the service was not found.
	 *
	 * @throws UnknownServiceException Indicates the service was not known.
	 */
	<R extends Service> R getService(Class<R> serviceRole);

	/**
	 * Retrieve a service by role.  If service is not found, but a {@link org.hibernate.service.spi.ServiceInitiator} is
	 * registered for this service role, the service will be initialized and returned.
	 * <p/>
	 * NOTE: We cannot return {@code <R extends Service<T>>} here because the service might come from the parent...
	 *
	 * @param serviceRole The service role
	 * @param <R> The service role type
	 *
	 * @return The requested service .
	 *
	 * @throws UnknownServiceException Indicates the service was not known.
	 * @throws NullServiceException Indicates the service was null.
	 */
	default <R extends Service> R requireService(Class<R> serviceRole) {
		final R service = getService( serviceRole );
		if ( service == null ) {
			throw new NullServiceException( serviceRole );
		}
		return service;
	}

	@Override
	void close();
}
