/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service.spi;

/**
 * Optional contract for services that wrap stuff that to which it is useful to have access.  For example, a service
 * that maintains a {@link javax.sql.DataSource} might want to expose access to the {@link javax.sql.DataSource} or
 * its {@link java.sql.Connection} instances.
 *
 * 可选的约定  - 为了包装服务填充 - 有时候访问这种东西是必要的, 例如 一个服务 包含了一个javax.sql.DataSource 可能想要暴露访问javax.sql.DataSource 或者 它的java.sql.Connection 实例 ...
 *
 * @author Steve Ebersole
 */
public interface Wrapped {
	/**
	 * Can this wrapped service be unwrapped as the indicated type?
	 *
	 * @param unwrapType The type to check.
	 *
	 * @return True/false.
	 */
	boolean isUnwrappableAs(Class<?> unwrapType);

	/**
	 * Unproxy the service proxy
	 *
	 * @param unwrapType The java type as which to unwrap this instance.
	 *
	 * @return The unwrapped reference
	 *
	 * @throws UnknownUnwrapTypeException if the service cannot be unwrapped as the indicated type
	 */
	<T> T unwrap(Class<T> unwrapType);
}
