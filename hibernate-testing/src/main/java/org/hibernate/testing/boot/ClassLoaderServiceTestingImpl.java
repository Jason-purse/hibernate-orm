/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.testing.boot;

import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;

/**
 * @author Steve Ebersole
 * 基于测试的类加载器服务实现 ...
 */
public class ClassLoaderServiceTestingImpl extends ClassLoaderServiceImpl {
	/**
	 * Singleton access
	 */
	public static final ClassLoaderServiceTestingImpl INSTANCE = new ClassLoaderServiceTestingImpl();
}
