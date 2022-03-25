/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.internal.util.collections;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Stack implementation exposing useful methods for Hibernate needs.
 * Hibernate 需要的栈的实现(暴露了许多有用的方法)
 *
 * @param <T> The type of things stored in the stack
 *
 * @author Steve Ebersole
 */
public interface Stack<T> {
	/**
	 * Push the new element on the top of the stack
	 */
	void push(T newCurrent);

	/**
	 * Pop (remove and return) the current element off the stack
	 */
	T pop();

	/**
	 * The element currently at the top of the stack
	 */
	T getCurrent();

	/**
	 * How many elements are currently on the stack?
	 */
	int depth();

	/**
	 * Are there no elements currently in the stack?
	 */
	boolean isEmpty();

	/**
	 * Remove all elements from the stack
	 */
	void clear();

	/**
	 * Visit all elements in the stack, starting with the root and working "forward"
	 * 查看所有 在 栈中的元素, 已root 开始 并向前工作
	 */
	void visitRootFirst(Consumer<T> action);

	/**
	 * Find an element on the stack and return a value.  The first non-null element
	 * returned from `action` stops the iteration and is returned from here
	 */
	<X> X findCurrentFirst(Function<T, X> action);
}
