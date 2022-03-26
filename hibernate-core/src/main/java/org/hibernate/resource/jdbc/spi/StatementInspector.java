/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.resource.jdbc.spi;

import java.io.Serializable;

/**
 * Contract to allow inspection (and swapping) of SQL to be prepared.
 * <p>
 *     检测一个SQL 是否能够被准备的约定
 * An implementation may be specified via the configuration property
 * {@value org.hibernate.cfg.AvailableSettings#STATEMENT_INSPECTOR}.
 *  // 一个实现也许能够被指定 - 通过设置  {@value org.hibernate.cfg.AvailableSettings#STATEMENT_INSPECTOR}.
 *
 * @see org.hibernate.boot.SessionFactoryBuilder#applyStatementInspector(StatementInspector)
 *
 * @author Steve Ebersole
 */
public interface StatementInspector extends Serializable {
	/**
	 * Inspect the given SQL, possibly returning a different SQL to be used instead.  Note that returning {@code null}
	 * is interpreted as returning the same SQL as was passed.
	 *	检测给定的SQL, 可能返回一个不同 的SQL(能够 被替代), 注意 返回null - 被认为返回相同的SQL
	 * @param sql The SQL to inspect
	 *
	 * @return The SQL to use; may be {@code null}
	 */
	String inspect(String sql);
}
