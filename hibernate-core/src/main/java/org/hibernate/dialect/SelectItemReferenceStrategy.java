/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.dialect;

/**
 * Strategies for referring to a select item.
 * 一个select item 的引用策略
 * @author Christian Beikov
 */
public enum SelectItemReferenceStrategy {
	/**
	 * The default strategy i.e. render the expression again.  默认策略,渲染表达式
	 */
	EXPRESSION,
	/**
	 * Refer to the item via its alias.  通过别名引用
	 */
	ALIAS,
	/**
	 * Refer to the item via its position.  通过位置引用
	 */
	POSITION;
}
