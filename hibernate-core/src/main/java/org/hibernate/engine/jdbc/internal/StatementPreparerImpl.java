/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.AssertionFailure;
import org.hibernate.ScrollMode;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.engine.jdbc.spi.StatementPreparer;
import org.hibernate.resource.jdbc.spi.JdbcObserver;
import org.hibernate.resource.jdbc.spi.LogicalConnectionImplementor;

/**
 * Standard implementation of StatementPreparer
 *
 * @author Steve Ebersole
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 * @author Brett Meyer
*/
class StatementPreparerImpl implements StatementPreparer {
	private final JdbcCoordinatorImpl jdbcCoordinator;
	private final JdbcServices jdbcServices;

	/**
	 * Construct a StatementPreparerImpl
	 *
	 * @param jdbcCoordinator The JdbcCoordinatorImpl
	 */
	StatementPreparerImpl(JdbcCoordinatorImpl jdbcCoordinator, JdbcServices jdbcServices) {
		this.jdbcCoordinator = jdbcCoordinator;
		this.jdbcServices = jdbcServices;
	}

	protected final SessionFactoryOptions settings() {
		return jdbcCoordinator.sessionFactory().getSessionFactoryOptions();
	}
	// 获取物理连接
	protected final Connection connection() {
		return logicalConnection().getPhysicalConnection();
	}

	protected final LogicalConnectionImplementor logicalConnection() {
		return jdbcCoordinator.getLogicalConnection();
	}

	protected final SqlExceptionHelper sqlExceptionHelper() {
		return jdbcServices.getSqlExceptionHelper();
	}
	
	@Override
	public Statement createStatement() {
		try {
			final Statement statement = connection().createStatement();
			jdbcCoordinator.getLogicalConnection().getResourceRegistry().register( statement, true );
			return statement;
		}
		catch ( SQLException e ) {
			throw sqlExceptionHelper().convert( e, "could not create statement" );
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql) {
		return buildPreparedStatementPreparationTemplate( sql, false ).prepareStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, final boolean isCallable) {
		jdbcCoordinator.executeBatch(); // 执行batch
		return buildPreparedStatementPreparationTemplate( sql, isCallable ).prepareStatement();  // 执行完毕 之后
	}
	// 构建preparedStatement的预编译模板
	private StatementPreparationTemplate buildPreparedStatementPreparationTemplate(String sql, final boolean isCallable) {
		return new StatementPreparationTemplate( sql ) {
			// 如果callable
			@Override
			protected PreparedStatement doPrepare() throws SQLException {
				return isCallable
						// 获取连接
						? connection().prepareCall( sql )
						: connection().prepareStatement( sql );
			}
		};
	}

	private void checkAutoGeneratedKeysSupportEnabled() {
		if ( ! settings().isGetGeneratedKeysEnabled() ) {
			throw new AssertionFailure( "getGeneratedKeys() support is not enabled" );
		}
	}

	@Override
	public PreparedStatement prepareStatement(String sql, final int autoGeneratedKeys) {
		if ( autoGeneratedKeys == PreparedStatement.RETURN_GENERATED_KEYS ) {
			checkAutoGeneratedKeysSupportEnabled();
		}
		jdbcCoordinator.executeBatch();
		return new StatementPreparationTemplate( sql ) {
			public PreparedStatement doPrepare() throws SQLException {
				return connection().prepareStatement( sql, autoGeneratedKeys );
			}
		}.prepareStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql, final String[] columnNames) {
		checkAutoGeneratedKeysSupportEnabled();
		jdbcCoordinator.executeBatch();
		return new StatementPreparationTemplate( sql ) {
			public PreparedStatement doPrepare() throws SQLException {
				return connection().prepareStatement( sql, columnNames );
			}
		}.prepareStatement();
	}

	@Override
	public PreparedStatement prepareQueryStatement(
			String sql,
			final boolean isCallable,
			final ScrollMode scrollMode) {
		if ( scrollMode != null && !scrollMode.equals( ScrollMode.FORWARD_ONLY ) ) {
			if ( ! settings().isScrollableResultSetsEnabled() ) {
				throw new AssertionFailure("scrollable result sets are not enabled");
			}
			final PreparedStatement ps = new QueryStatementPreparationTemplate( sql ) {
				public PreparedStatement doPrepare() throws SQLException {
						return isCallable
								? connection().prepareCall( sql, scrollMode.toResultSetType(), ResultSet.CONCUR_READ_ONLY )
								: connection().prepareStatement( sql, scrollMode.toResultSetType(), ResultSet.CONCUR_READ_ONLY );
				}
			}.prepareStatement();
			jdbcCoordinator.registerLastQuery( ps );
			return ps;
		}
		else {
			final PreparedStatement ps = new QueryStatementPreparationTemplate( sql ) {
				public PreparedStatement doPrepare() throws SQLException {
						return isCallable
								? connection().prepareCall( sql )
								: connection().prepareStatement( sql );
				}
			}.prepareStatement();
			jdbcCoordinator.registerLastQuery( ps );
			return ps;
		}
	}

	private abstract class StatementPreparationTemplate {
		protected final String sql;
		// 进入的SQL
		protected StatementPreparationTemplate(String incomingSql) {
			//
			final String inspectedSql = jdbcCoordinator.getJdbcSessionOwner()
					.getJdbcSessionContext()
					.getStatementInspector()
					.inspect( incomingSql );
			this.sql = inspectedSql == null ? incomingSql : inspectedSql;
		}

		// 准备语句
		public PreparedStatement prepareStatement() {
			try {
				jdbcServices.getSqlStatementLogger().logStatement( sql );

				final PreparedStatement preparedStatement;
				final JdbcObserver observer = jdbcCoordinator.getJdbcSessionOwner().getJdbcSessionContext().getObserver();
				try {
					// 观察者
					observer.jdbcPrepareStatementStart();
					preparedStatement = doPrepare(); // 进行prepared
					setStatementTimeout( preparedStatement );
				}
				finally {
					observer.jdbcPrepareStatementEnd();
				}
				// 后置处理
				postProcess( preparedStatement );
				return preparedStatement;
			}
			catch ( SQLException e ) {
				throw sqlExceptionHelper().convert( e, "could not prepare statement", sql );
			}
		}

		protected abstract PreparedStatement doPrepare() throws SQLException;

		// 后置处理
		public void postProcess(PreparedStatement preparedStatement) throws SQLException {
			//
			jdbcCoordinator.getLogicalConnection().getResourceRegistry().register( preparedStatement, true );
//			logicalConnection().notifyObserversStatementPrepared();
		}
		// 设置statement Timeout
		private void setStatementTimeout(PreparedStatement preparedStatement) throws SQLException {
			// jdbc 协调器
			final int remainingTransactionTimeOutPeriod = jdbcCoordinator.determineRemainingTransactionTimeOutPeriod();
			if ( remainingTransactionTimeOutPeriod > 0 ) {  // 剩余的事务超时时间段 ..
				preparedStatement.setQueryTimeout( remainingTransactionTimeOutPeriod );
			}
		}
	}

	private abstract class QueryStatementPreparationTemplate extends StatementPreparationTemplate {
		protected QueryStatementPreparationTemplate(String sql) {
			super( sql );
		}

		public void postProcess(PreparedStatement preparedStatement) throws SQLException {
			super.postProcess( preparedStatement );
			setStatementFetchSize( preparedStatement );
		}
	}

	private void setStatementFetchSize(PreparedStatement statement) throws SQLException {
		if ( settings().getJdbcFetchSize() != null ) {
			statement.setFetchSize( settings().getJdbcFetchSize() );
		}
	}

}
