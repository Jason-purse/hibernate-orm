/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast.tree.predicate;

/**
 * Something that can contain predicates
 *
 * 有些 能够包含一些条件
 * @author Steve Ebersole
 */
public interface PredicateContainer {
	/**
	 *  给这个容器应用 条件
	 * Apply a predicate to this container
	 */
	void applyPredicate(Predicate predicate);
}
