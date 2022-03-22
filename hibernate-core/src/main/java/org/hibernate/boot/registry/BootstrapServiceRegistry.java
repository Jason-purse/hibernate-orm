/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.registry;

import org.hibernate.service.ServiceRegistry;

/**
 * Provides the most basic services such as class loading. Other
 * configuration-time objects such as {@link org.hibernate.boot.MetadataSources},
 * {@link StandardServiceRegistryBuilder}, and {@link org.hibernate.cfg.Configuration}
 * all depend on an instance of {@code BootstrapServiceRegistry}.
 *
 * 提供大多数基本服务 -> 例如类加载
 * // 其他配置时的对象 例如MetadataSources / StandardServiceRegistryBuilder 以及Configuration 都依赖这个类的实例 ...
 * <p>
 * An instance may be obtained using {@link BootstrapServiceRegistryBuilder#build()}.
 * <p>
 * Specialized from {@link ServiceRegistry} mainly for type safety.
 *
 * 从ServiceRegistry 规范化主要是为了类型安全
 *
 * @see BootstrapServiceRegistryBuilder
 *
 * @author Steve Ebersole
 */
public interface BootstrapServiceRegistry extends ServiceRegistry {
}
