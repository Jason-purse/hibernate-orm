/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.registry.classloading.internal;

/**
 * Defines when the lookup in the current thread context {@link ClassLoader} should be
 * done according to the other ones.
 * 定义 - 当在当前线程上下文类加载器中寻找时应该 根据其他某一个完成 ...
 * 
 * @author Cédric Tabin
 */
public enum TcclLookupPrecedence {
	/**
	 * The current thread context {@link ClassLoader} will never be used during
	 * the class lookup.
	 */
	NEVER,

	/**
	 * 此类将在线程上下文中 查询  先于其他 ClassLoader 查询 ...
	 * The class lookup will be done in the thread context {@link ClassLoader} prior
	 * to the other {@code ClassLoader}s.
	 */
	BEFORE,

	/**
	 * 其他类加载器没有发现 才进行当前线程上下文  类查询 ...
	 * The class lookup will be done in the thread context {@link ClassLoader} if
	 * the former hasn't been found in the other {@code ClassLoader}s.
	 * This is the default value.
	 */
	AFTER
}
