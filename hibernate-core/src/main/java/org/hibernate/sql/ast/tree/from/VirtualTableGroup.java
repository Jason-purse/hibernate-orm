/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast.tree.from;

/**
 * Marker interface for TableGroup impls that are virtual - should not be rendered
 * into the SQL.
 * 虚拟性的TableGroup 的标记接口, 应该 不会在SQL 中渲染
 * @author Steve Ebersole
 */
public interface VirtualTableGroup extends TableGroup {
}
