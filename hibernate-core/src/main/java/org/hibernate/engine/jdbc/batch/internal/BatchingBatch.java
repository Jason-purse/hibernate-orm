/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.batch.internal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.batch.spi.BatchKey;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.internal.CoreMessageLogger;

import org.hibernate.resource.jdbc.spi.JdbcObserver;
import org.jboss.logging.Logger;

/**
 * A {@link org.hibernate.engine.jdbc.batch.spi.Batch} implementation which does bathing based on a given size.  Once
 * the batch size is reached for a statement in the batch, the entire batch is implicitly executed.
 *
 * Batch 实现  - 基于给定的长度, 一旦 batch 尺寸 达到上限(statement的个数), 那么  这整个batch 将隐式 的执行 ..
 *
 * @author Steve Ebersole
 */
public class BatchingBatch extends AbstractBatchImpl {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			BatchingBatch.class.getName()
	);

	// IMPL NOTE : Until HHH-5797 is fixed, there will only be 1 statement in a batch

	private int batchSize;
	private final int configuredBatchSize;
	private int batchPosition;  // batch可以执行的位置 (小于等于 0 标识不可以执行)
	private boolean batchExecuted;
	private int statementPosition;

	/**
	 * Constructs a BatchingBatch
	 *
	 * @param key The batch key
	 * @param jdbcCoordinator The JDBC jdbcCoordinator
	 * @param batchSize The batch size.
	 */
	public BatchingBatch(
			BatchKey key,
			JdbcCoordinator jdbcCoordinator,
			int batchSize) {
		super( key, jdbcCoordinator );
		if ( ! key.getExpectation().canBeBatched() ) {
			throw new HibernateException( "attempting to batch an operation which cannot be batched" );
		}
		this.batchSize = batchSize;
		this.configuredBatchSize = batchSize;
	}

	private String currentStatementSql; // 当前 statementSql
	private PreparedStatement currentStatement;

	@Override
	public PreparedStatement getBatchStatement(String sql, boolean callable) {
		currentStatementSql = sql;
		int previousBatchSize = getStatements().size(); // 之前的尺寸
		currentStatement = super.getBatchStatement( sql, callable ); // 获取当前的statement
		int currentBatchSize = getStatements().size(); // 获取当前Batch 的尺寸
		if ( currentBatchSize > previousBatchSize ) { // 如果 大于
			this.batchSize = this.configuredBatchSize * currentBatchSize; // 扩展
		}
		return currentStatement; // 获取当前的语句
	}

	@Override
	public void addToBatch() {
		try {
			currentStatement.addBatch();
		}
		catch ( SQLException e ) {
			abortBatch( e );
			LOG.debug( "SQLException escaped proxy", e );
			throw sqlExceptionHelper().convert( e, "could not perform addBatch", currentStatementSql );
		}
		catch (RuntimeException e) {
			abortBatch( e );
			throw e;
		}
		statementPosition++;
		if ( statementPosition >= getKey().getBatchedStatementCount() ) {
			batchPosition++;
			if ( batchPosition == batchSize ) {
				notifyObserversImplicitExecution();
				performExecution();
				batchPosition = 0;
				batchExecuted = true;
			}
			statementPosition = 0;
		}
	}

	@Override
	protected void doExecuteBatch() {
		if (batchPosition == 0 ) { // batch 位置等于 0
			if(! batchExecuted) {
				LOG.debug( "No batched statements to execute" );
			}
		}
		else {
			performExecution();
		}
	}

	private void performExecution() {
		LOG.debugf( "Executing batch size: %s", batchPosition );
		final JdbcObserver observer = getJdbcCoordinator().getJdbcSessionOwner().getJdbcSessionContext().getObserver();
		try {
			for ( Map.Entry<String,PreparedStatement> entry : getStatements().entrySet() ) {
				final String sql = entry.getKey(); // key 为SQL
				try {
					final PreparedStatement statement = entry.getValue(); // 获取PreparedStatement
					final int[] rowCounts;
					try {
						observer.jdbcExecuteBatchStart();
						rowCounts = statement.executeBatch(); // 语句批量执行
					}
					finally {
						observer.jdbcExecuteBatchEnd();
					}
					checkRowCounts( rowCounts, statement, sql ); // 检查行 个数
				}
				catch ( SQLException e ) {
					abortBatch( e );
					LOG.unableToExecuteBatch( e, sql );
					throw sqlExceptionHelper().convert( e, "could not execute batch", sql );
				}
				catch ( RuntimeException re ) {
					abortBatch( re );
					LOG.unableToExecuteBatch( re, sql );
					throw re;
				}
			}
		}
		finally {
			batchPosition = 0;
		}
	}

	// 检查行数
	private void checkRowCounts(int[] rowCounts, PreparedStatement ps, String statementSQL) throws SQLException, HibernateException {
		final int numberOfRowCounts = rowCounts.length;
		if ( batchPosition != 0 && numberOfRowCounts != batchPosition / getStatements().size() ) { // batchPosition != 0 并且
			LOG.unexpectedRowCounts(); // 不期待的row Counts
		}
		for ( int i = 0; i < numberOfRowCounts; i++ ) { //受影响的函数 ....
			// 获取输出期待 并验证
			getKey().getExpectation().verifyOutcome( rowCounts[i], ps, i, statementSQL ); //
		}
	}
}
