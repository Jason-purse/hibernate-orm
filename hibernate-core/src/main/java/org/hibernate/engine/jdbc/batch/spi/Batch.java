/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.batch.spi;
import java.sql.PreparedStatement;

/**
 * Conceptually models a batch.
 * 一个batch 的 概念模型
 * <p/>
 *
 * 不像直接在JDBC中,这里 我们增加了一种能力能够 一次批量集中多个语句 ...
 * 底层的JDBC 关联的多个PreparedStatement 对象(或者每一个DML 字符串) 将维护在这个batch对象中 ...
 * Unlike directly in JDBC, here we add the ability to batch together multiple statements at a time.  In the underlying
 * JDBC this correlates to multiple {@link PreparedStatement} objects (one for each DML string) maintained within the
 * batch.
 *
 * @author Steve Ebersole
 */
public interface Batch {
	/**
	 * Retrieves the object being used to key (uniquely identify) this batch.
	 *
	 * @return The batch key.
	 */
	BatchKey getKey();

	/**
	 * Adds an observer to this batch.
	 *
	 * @param observer The batch observer.
	 */
	void addObserver(BatchObserver observer);

	/**
	 * Get a statement which is part of the batch, creating if necessary (and storing for next time).
	 *
	 * @param sql The SQL statement.
	 * @param callable Is the SQL statement callable?
	 *
	 * @return The prepared statement instance, representing the SQL statement.
	 */
	PreparedStatement getBatchStatement(String sql, boolean callable);

	/**
	 * Indicates completion of the current part of the batch.
	 */
	void addToBatch();

	/**
	 * Execute this batch.
	 * 执行 这个batch
	 */
	void execute();

	/**
	 * Used to indicate that the batch instance is no longer needed and that, therefore, it can release its
	 * resources.
	 */
	void release();
}
