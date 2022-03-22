/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.query.sqm.mutation.internal.temptable;

import org.hibernate.dialect.temptable.TemporaryTable;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.spi.DomainQueryExecutionContext;
import org.hibernate.query.sqm.internal.DomainParameterXref;
import org.hibernate.query.sqm.mutation.spi.SqmMultiTableMutationStrategy;
import org.hibernate.query.sqm.tree.delete.SqmDeleteStatement;
import org.hibernate.query.sqm.tree.update.SqmUpdateStatement;

/**
 * This is a strategy that mimics temporary tables for databases which do not support
 * temporary tables.  It follows a pattern similar to the ANSI SQL definition of global
 * temporary table using a "session id" column to segment rows from the various sessions.
 *
 * 这是一种 模仿临时表的策略 - 主要是针对数据库不支持临时表的情况 下使用 ;
 * 它 遵循一个模式  类似于ANSI SQL 对于全局临时表的定义 - 使用session id 字段去分割行 - 从不同的sessions中 ...
 *
 * @author Steve Ebersole
 */
public class PersistentTableMutationStrategy extends PersistentTableStrategy implements SqmMultiTableMutationStrategy {

	public PersistentTableMutationStrategy(
			TemporaryTable idTable,
			SessionFactoryImplementor sessionFactory) {
		super( idTable, sessionFactory );
	}

	@Override
	public int executeUpdate(
			SqmUpdateStatement<?> sqmUpdate,
			DomainParameterXref domainParameterXref,
			DomainQueryExecutionContext context) {
		return new TableBasedUpdateHandler(
				sqmUpdate,
				domainParameterXref,
				getTemporaryTable(),
				getSessionFactory().getJdbcServices().getDialect().getTemporaryTableAfterUseAction(),
				session -> session.getSessionIdentifier().toString(),
				getSessionFactory()
		).execute( context );
	}

	@Override
	public int executeDelete(
			SqmDeleteStatement<?> sqmDelete,
			DomainParameterXref domainParameterXref,
			DomainQueryExecutionContext context) {
		return new TableBasedDeleteHandler(
				sqmDelete,
				domainParameterXref,
				getTemporaryTable(),
				getSessionFactory().getJdbcServices().getDialect().getTemporaryTableAfterUseAction(),
				session -> session.getSessionIdentifier().toString(),
				getSessionFactory()
		).execute( context );
	}
}
