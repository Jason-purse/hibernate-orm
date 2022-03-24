/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast.spi;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.mapping.JdbcMappingContainer;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.sql.ast.tree.expression.Expression;
import org.hibernate.sql.results.jdbc.spi.JdbcValuesMetadata;
import org.hibernate.sql.results.jdbc.spi.RowProcessingState;
import org.hibernate.sql.ast.SqlAstWalker;
import org.hibernate.type.descriptor.ValueExtractor;

/**
 * @asciidoclet
 *
 * Represents a selection at the SQL/JDBC level.  Essentially made up of:
 *		呈现了一个SQL /JDBC层级上的selection , 由  getJdbcValueExtractor : 如何从JDBC中读取数据(概念类似于方法引用)   getValuesArrayPosition 当前数据在jdbc values中的未知 ... 根据位置获取数据
 * 		{@link #getJdbcValueExtractor}:: How to read a value from JDBC (conceptually similar to a method reference)
 * 		{@link #getValuesArrayPosition}:: The position for this selection in relation to the "JDBC values array" (see {@link RowProcessingState#getJdbcValue})
 * 		{@link #getJdbcResultSetIndex()}:: The position for this selection in relation to the JDBC object (ResultSet, etc) // 获取结果集的index(相关jdbc对象(结果集)的selection 的位置)
 *
 * Additional support for allowing a selection to "prepare" itself prior to first use is defined through
 * {@link #prepare}.  This is generally only used for NativeQuery execution. // 也有额外的支持  它可以首先在第一次使用之前通过prepare 预定义 ... 通常被NativeQuery 执行所使用 ..
 *
 * @author Steve Ebersole
 */
public interface SqlSelection extends SqlAstNode {
	/**
	 * Get the extractor that can be used to extract JDBC values for this selection
	 */
	ValueExtractor getJdbcValueExtractor();

	/**
	 * Get the position within the "JDBC values" array (0-based).  Negative indicates this is
	 * not a "real" selection
	 */
	int getValuesArrayPosition();

	/**
	 * Get the JDBC position (1-based)
	 */
	default int getJdbcResultSetIndex() {
		return getValuesArrayPosition() + 1;
	}

	/**
	 * The underlying expression.
	 */
	Expression getExpression();

	/**
	 * Get the type of the expression
	 */
	JdbcMappingContainer getExpressionType();

	void accept(SqlAstWalker sqlAstWalker);

	SqlSelection resolve(JdbcValuesMetadata jdbcResultsMetadata, SessionFactoryImplementor sessionFactory);
}
