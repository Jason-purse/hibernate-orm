/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.jdbc;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;

/**
 * Defines an expected DML operation outcome.
 * 定义一个期待的DML 操作输出 ...
 * @author Steve Ebersole
 */
public interface Expectation {
	/**
	 * Perform verification of the outcome of the RDBMS operation based on
	 * the type of expectation defined.
	 * 基于定义的期待的类型 执行RDMS 操作的输出验证 ...
	 * @param rowCount The RDBMS reported "number of rows affected". // rdbms 输出的多少条被影响了 。。
	 * @param statement The statement representing the operation // 这个操作的语句 ..
	 * @param batchPosition The position in the batch (if batching) // 在batch中的位置(如果在batch中)
	 * @param statementSQL The SQL backing the prepared statement, for logging purposes // 预编译语句背后的SQL, 为了日志纪录 ..
	 * @throws SQLException Exception from the JDBC driver
	 * @throws HibernateException Problem processing the outcome.
	 */
	void verifyOutcome(int rowCount, PreparedStatement statement, int batchPosition, String statementSQL) throws SQLException, HibernateException;

	/**
	 * Perform any special statement preparation.
	 *	 执行任何指定的语句 准备
	 * @param statement The statement to be prepared
	 * @return The number of bind positions consumed (if any)
	 * @throws SQLException Exception from the JDBC driver
	 * @throws HibernateException Problem performing preparation.
	 */
	int prepare(PreparedStatement statement) throws SQLException, HibernateException;

	/**
	 * Is it acceptable to combiner this expectation with statement batching?
	 *
	 * @return True if batching can be combined with this expectation; false otherwise.
	 */
	boolean canBeBatched();
}
