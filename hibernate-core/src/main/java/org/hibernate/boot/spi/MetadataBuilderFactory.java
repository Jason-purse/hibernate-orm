/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.spi;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;

/**
 * An extension point for integrators that wish to hook into the process of how a {@link Metadata} is built. Intended as
 * a "discoverable service" ({@link java.util.ServiceLoader}). There can be at most one implementation discovered that
 * returns a non-null SessionFactoryBuilder.
 *
 * 一个integrators的扩展点 - 希望回调 Metadata的构建处理过程中..
 * 打算通过ServiceLoader 发现服务 ... 大多数情况能发现一个返回非空的SessionFactoryBuilder
 *
 * @author Gunnar Morling
 */
public interface MetadataBuilderFactory {

	/**
	 * Creates a {@link MetadataBuilderImplementor}.
	 *
	 * @param metadatasources The current metadata sources
	 * @param defaultBuilder The default builder, may be used as a delegate
	 * @return a new metadata builder
	 */
	MetadataBuilderImplementor getMetadataBuilder(MetadataSources metadatasources, MetadataBuilderImplementor defaultBuilder);
}
