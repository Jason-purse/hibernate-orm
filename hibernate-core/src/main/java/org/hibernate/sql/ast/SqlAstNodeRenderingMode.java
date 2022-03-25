/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast;

import org.hibernate.sql.ast.tree.SqlAstNode;

/**
 * The rendering mode to use for {@link SqlAstNode}.
 * 使用SqlAstNode 的渲染mode
 * Some functions/contexts require the use of literals/expressions rather than parameters
 * like for example the `char` function in Derby which requires the length as literal.
 * 某些函数 / 上下文 需要使用literals  / expressions 而不是参数(例如 'char' 函数 例如“字符”功能在Derby中需要文字的长度。)
 * Another example is a function that renders a function argument into a subquery select and group by item. // 其他例子 一个函数 渲染函数参数到子查询select 以及 group by item;
 * It can use {@link #INLINE_PARAMETERS} so that a database can match such a select item to a group by item. // 它能够使用inline_parameters 那么数据库能够匹配这样的一个select item 到group by item;
 * Without this, such queries would result in a query error. // 否则查询将会导致错误
 * 
 * @author Christian Beikov
 * @see SqlAstTranslator#render(SqlAstNode, SqlAstNodeRenderingMode) 
 */
public enum SqlAstNodeRenderingMode {
	/**
	 * Render node as is.
	 */
	DEFAULT,

	/**
	 * Render parameters as literals. 将参数渲染为文本
	 * All parameters within the {@link SqlAstNode} are rendered as literals.
	 */
	INLINE_PARAMETERS,

	/**
	 * Render all nested parameters as literals.
	 * All parameters within the {@link SqlAstNode} are rendered as literals.  渲染内嵌的参数都是文本
	 */
	INLINE_ALL_PARAMETERS,

	/**
	 * Don't render plain parameters. Render it as literal or as expression.
	 * If the {@link SqlAstNode} to render is a parameter,
	 * it will be rendered either as literal or wrapped into a semantically equivalent expression
	 * such that it doesn't appear as plain parameter. // 不渲染简单的参数,渲染它们作为文本或者表达式 ..
	 */ // 如果SqlAstNode 渲染的是一个参数,它将会渲染为字符串  或者包装到一个等价的语义表达式中,例如它不作为一个简单的参数出现..
	NO_PLAIN_PARAMETER
}
