/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.batch.spi;

import org.hibernate.jdbc.Expectation;

/**
 * Unique key for batch identification.
 * 每一个batch 标识 的唯一 key
 * @author Steve Ebersole
 */
public interface BatchKey {
	/**
	 * How many statements will be in this batch?
	 *
	 *这个batch 中有多少条 语句
	 * <p/>
	 * Note that this is distinctly different than the size of the batch.  注意  它不同于batch的尺寸...
	 *
	 * @return The number of statements.
	 */
	int getBatchedStatementCount();

	/**
	 * Get the expectation pertaining to the outcome of the {@link Batch} associated with this key.
	 * 获取与此键关联的 {@link Batch} 的结果相关的期望值。
	 * @return The expectations
	 */
	Expectation getExpectation();
}
