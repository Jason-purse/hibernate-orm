/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.spi;

import org.hibernate.boot.SessionFactoryBuilder;

/**
 * An extension point for integrators that wish to hook into the process of how a SessionFactory
 * is built.  Intended as a "discoverable service" ({@link java.util.ServiceLoader}).  There can
 * be at most one implementation discovered that returns a non-null SessionFactoryBuilder.
 *
 * 一个扩展点  -  integrators 希望参与到 SessionFactory 构建的过程中
 * // 通过ServiceLoader 进行服务发现 .
 * // 大多数实现返回一个非空的SessionFactoryBuilder ;;
 *
 * @author Steve Ebersole
 */
public interface SessionFactoryBuilderFactory {
	/**
	 * The contract method.  Return the SessionFactoryBuilder.  May return {@code null}
	 *
	 * @param metadata The metadata from which we will be building a SessionFactory.
	 * @param defaultBuilder The default SessionFactoryBuilder instance.  If the SessionFactoryBuilder being built
	 * here needs to use this passed SessionFactoryBuilder instance it is the responsibility of the built
	 * SessionFactoryBuilder impl to delegate configuration calls to the passed default impl.
	 *
	 *                       如果SessionFactoryBuilder 可能在这里构建的需要使用传入的SessionFactoryBuilder
	 *                       // 主要是基于装饰器模式(或者代理 模式)   将代理配置调用到 默认的impl;
	 *
	 * @return The SessionFactoryBuilder, or {@code null}
	 */
	SessionFactoryBuilder getSessionFactoryBuilder(MetadataImplementor metadata, SessionFactoryBuilderImplementor defaultBuilder);
}
