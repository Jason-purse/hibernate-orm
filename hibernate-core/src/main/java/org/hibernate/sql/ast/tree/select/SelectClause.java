/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast.tree.select;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.sql.ast.spi.SqlSelection;
import org.hibernate.sql.ast.SqlAstWalker;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.query.sqm.sql.internal.DomainResultProducer;

/**
 * The SELECT CLAUSE in the SQL AST.  Each selection here is a
 * {@link DomainResultProducer}
 *
 * SQL 抽象语法树的Select 子句  每一个selection  在这里都是一个DomainResultProducer.
 *
 * @author Steve Ebersole
 */
public class SelectClause implements SqlAstNode {
	private boolean distinct;

	private final List<SqlSelection> sqlSelections;

	public SelectClause() {
		this.sqlSelections = new ArrayList<>();
	}

	public SelectClause(int estimateSelectionSize) {
		this.sqlSelections = new ArrayList<>( estimateSelectionSize );
	}

	public void makeDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void addSqlSelection(SqlSelection sqlSelection) {
		sqlSelections.add( sqlSelection );
	}

	public List<SqlSelection> getSqlSelections() {
		return sqlSelections;
	}

	@Override
	public void accept(SqlAstWalker sqlTreeWalker) {
		sqlTreeWalker.visitSelectClause( this );
	}
}
