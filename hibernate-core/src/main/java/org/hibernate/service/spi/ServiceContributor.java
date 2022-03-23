/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service.spi;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Contract for contributing services.
 *
 * @implSpec Implementations can be auto-discovered via Java's {@link java.util.ServiceLoader}
 * mechanism.
 *
 * 贡献服务的约定
 *
 * @author Steve Ebersole
 */
public interface ServiceContributor {
	/**
	 * Contribute services to the indicated registry builder.
	 * 贡献服务到 指定的注册表构建器
	 *
	 * @param serviceRegistryBuilder The builder to which services (or initiators) should be contributed.
	 */
	void contribute(StandardServiceRegistryBuilder serviceRegistryBuilder);
}
