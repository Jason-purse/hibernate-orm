/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.env.internal;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectFactory;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.log.DeprecationLogger;
import org.hibernate.internal.util.NullnessHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import org.jboss.logging.Logger;

/**
 * @author Steve Ebersole
 */
public class JdbcEnvironmentInitiator implements StandardServiceInitiator<JdbcEnvironment> {
	private static final CoreMessageLogger log = Logger.getMessageLogger(
			CoreMessageLogger.class,
			JdbcEnvironmentInitiator.class.getName()
	);

	public static final JdbcEnvironmentInitiator INSTANCE = new JdbcEnvironmentInitiator();

	@Override
	public Class<JdbcEnvironment> getServiceInitiated() {
		return JdbcEnvironment.class;
	}

	@Override
	public JdbcEnvironment initiateService(Map<String, Object> configurationValues, ServiceRegistryImplementor registry) {
		// 获取 方言工厂
		final DialectFactory dialectFactory = registry.getService( DialectFactory.class );

		// JDBC 环境  到底是哪一个环境 ..
		//  hibernate.temp.use_jdbc_metadata_defaults  是一个临时的魔术值
		// 'hibernate.temp.use_jdbc_metadata_defaults' is a temporary magic value.
		// 这打算有意缓解未来发展, 因此它是不会定义为一个环境常量的 ...
		// The need for it is intended to be alleviated with future development, thus it is
		// not defined as an Environment constant...
		//
		// 它被用来控制 我们是否应该考虑JDBC元数据来决定某些默认的values   它是有用的如果数据库不可用...  然后以此来 决定 ..
		// it is used to control whether we should consult the JDBC metadata to determine
		// certain default values; it is useful to *not* do this when the database
		// may not be available (mainly in tools usage).
		// 通过ConfigurationHelper 获取用户初始配置数据 ...
		final boolean useJdbcMetadata = ConfigurationHelper.getBoolean(
				"hibernate.temp.use_jdbc_metadata_defaults",
				configurationValues,
				true
		);
		// 非空判断帮助器
		final Object dbName = NullnessHelper.coalesceSuppliedValues(
				//
				() -> configurationValues.get( AvailableSettings.JAKARTA_HBM2DDL_DB_NAME ),
				() -> {
					// 老的... 返回第一个非空
					final Object value = configurationValues.get( AvailableSettings.DIALECT_DB_NAME );
					if ( value != null ) {
						// 发出警告,下次别再使用这个了 ... 提示作用
						DeprecationLogger.DEPRECATION_LOGGER.deprecatedSetting(
								AvailableSettings.DIALECT_DB_NAME,
								AvailableSettings.JAKARTA_HBM2DDL_DB_NAME
						);
					}
					return value;
				}
		);
		// 名称不为空
		if ( dbName != null ) {
			// 版本
			final String dbVersion = NullnessHelper.coalesceSuppliedValues(
					() -> (String) configurationValues.get( AvailableSettings.JAKARTA_HBM2DDL_DB_VERSION ),
					() -> {
						final Object value = configurationValues.get( AvailableSettings.DIALECT_DB_VERSION );
						if ( value != null ) {
							DeprecationLogger.DEPRECATION_LOGGER.deprecatedSetting(
									AvailableSettings.DIALECT_DB_VERSION,
									AvailableSettings.JAKARTA_HBM2DDL_DB_VERSION
							);
						}
						return (String) value;
					},
					() -> "0"
			);
			// 主要版本
			final int dbMajorVersion = NullnessHelper.coalesceSuppliedValues(
					() -> ConfigurationHelper.getInteger( AvailableSettings.JAKARTA_HBM2DDL_DB_MAJOR_VERSION, configurationValues ),
					() -> {
						final Integer value = ConfigurationHelper.getInteger(
								AvailableSettings.DIALECT_DB_MAJOR_VERSION,
								configurationValues
						);
						if ( value != null ) {
							DeprecationLogger.DEPRECATION_LOGGER.deprecatedSetting(
									AvailableSettings.DIALECT_DB_MAJOR_VERSION,
									AvailableSettings.JAKARTA_HBM2DDL_DB_MAJOR_VERSION
							);
						}
						return value;
					},
					() -> 0
			);

			// minor 版本
			final int dbMinorVersion = NullnessHelper.coalesceSuppliedValues(
					() -> ConfigurationHelper.getInteger( AvailableSettings.JAKARTA_HBM2DDL_DB_MINOR_VERSION, configurationValues ),
					() -> {
						final Integer value = ConfigurationHelper.getInteger(
								AvailableSettings.DIALECT_DB_MINOR_VERSION,
								configurationValues
						);
						if ( value != null ) {
							DeprecationLogger.DEPRECATION_LOGGER.deprecatedSetting(
									AvailableSettings.DIALECT_DB_MINOR_VERSION,
									AvailableSettings.JAKARTA_HBM2DDL_DB_MINOR_VERSION
							);
						}
						return value;
					},
					() -> 0
			);
			// 返回一个JDBC环境
			// 通过方言工厂构建 方言
			return new JdbcEnvironmentImpl( registry, dialectFactory.buildDialect(
					configurationValues, // 配置的值放入 ...
					// 给出方言解析结果 ....
					() -> new DialectResolutionInfo() {
						@Override
						public String getDatabaseName() {
							return (String) dbName;
						}

						@Override
						public String getDatabaseVersion() {
							return dbVersion;
						}

						@Override
						public int getDatabaseMajorVersion() {
							return dbMajorVersion;
						}

						@Override
						public int getDatabaseMinorVersion() {
							return dbMinorVersion;
						}

						@Override
						public String getDriverName() {
							return "";
						}

						@Override
						public int getDriverMajorVersion() {
							return 0;
						}

						@Override
						public int getDriverMinorVersion() {
							return 0;
						}

						@Override
						public String getSQLKeywords() {
							return "";
						}
					}
			) );
		}
		// 是否使用jdbcMetadata
		else if ( useJdbcMetadata ) {
			// 仅当使用JDBC 元数据的时候 才构建一个连接访问  进行检测
			final JdbcConnectionAccess jdbcConnectionAccess = buildJdbcConnectionAccess( configurationValues, registry );
			try {
				// 获取一个连接 ...
				final Connection connection = jdbcConnectionAccess.obtainConnection();
				try {
					// 获取数据库元数据
					final DatabaseMetaData dbmd = connection.getMetaData();
					if ( log.isDebugEnabled() ) {
						log.debugf(
								"Database ->\n"
										+ "       name : %s\n"
										+ "    version : %s\n"
										+ "      major : %s\n"
										+ "      minor : %s",
								dbmd.getDatabaseProductName(),
								dbmd.getDatabaseProductVersion(),
								dbmd.getDatabaseMajorVersion(),
								dbmd.getDatabaseMinorVersion()
						);
						log.debugf(
								"Driver ->\n"
										+ "       name : %s\n"
										+ "    version : %s\n"
										+ "      major : %s\n"
										+ "      minor : %s",
								dbmd.getDriverName(),
								dbmd.getDriverVersion(),
								dbmd.getDriverMajorVersion(),
								dbmd.getDriverMinorVersion()
						);
						log.debugf( "JDBC version : %s.%s", dbmd.getJDBCMajorVersion(), dbmd.getJDBCMinorVersion() );
					}
					// 构建 方言
					final Dialect dialect = dialectFactory.buildDialect(
							configurationValues,
							() -> {
								try {
									return new DatabaseMetaDataDialectResolutionInfoAdapter( connection.getMetaData() );
								}
								catch ( SQLException sqlException ) {
									throw new HibernateException(
											"Unable to access java.sql.DatabaseMetaData to determine appropriate Dialect to use",
											sqlException
									);
								}
							}
					);
					return new JdbcEnvironmentImpl(
							registry,
							dialect,
							dbmd,
							jdbcConnectionAccess
					);
				}
				catch (SQLException e) {
					log.unableToObtainConnectionMetadata( e );
				}
				finally {
					try {
						jdbcConnectionAccess.releaseConnection( connection );
					}
					catch (SQLException ignore) {
					}
				}
			}
			catch (Exception e) {
				log.unableToObtainConnectionToQueryMetadata( e );
			}
		}

		// if we get here, either we were asked to not use JDBC metadata or accessing the JDBC metadata failed.
		return new JdbcEnvironmentImpl( registry, dialectFactory.buildDialect( configurationValues, null ) );
	}

	private JdbcConnectionAccess buildJdbcConnectionAccess(Map<?,?> configValues, ServiceRegistryImplementor registry) {
		//
		if ( !configValues.containsKey( AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER ) ) {
			// 没有 就只有从服务中获取 ....
			ConnectionProvider connectionProvider = registry.getService( ConnectionProvider.class );
			return new ConnectionProviderJdbcConnectionAccess( connectionProvider );
		}
		else {
			// 支持多住户 ...
			// 直接指定 ...
			final MultiTenantConnectionProvider multiTenantConnectionProvider = registry.getService( MultiTenantConnectionProvider.class );
			return new MultiTenantConnectionProviderJdbcConnectionAccess( multiTenantConnectionProvider );
		}
	}

	// 构建顶级的jdbc 连接Access 通过它访问连接
	public static JdbcConnectionAccess buildBootstrapJdbcConnectionAccess(
			boolean multiTenancyEnabled,
			ServiceRegistryImplementor registry) {
		if ( !multiTenancyEnabled ) {
			ConnectionProvider connectionProvider = registry.getService( ConnectionProvider.class );
			return new ConnectionProviderJdbcConnectionAccess( connectionProvider );
		}
		else {
			final MultiTenantConnectionProvider multiTenantConnectionProvider = registry.getService( MultiTenantConnectionProvider.class );
			return new MultiTenantConnectionProviderJdbcConnectionAccess( multiTenantConnectionProvider );
		}
	}

	public static class ConnectionProviderJdbcConnectionAccess implements JdbcConnectionAccess {
		private final ConnectionProvider connectionProvider;

		public ConnectionProviderJdbcConnectionAccess(ConnectionProvider connectionProvider) {
			this.connectionProvider = connectionProvider;
		}

		public ConnectionProvider getConnectionProvider() {
			return connectionProvider;
		}

		@Override
		public Connection obtainConnection() throws SQLException {
			return connectionProvider.getConnection();
		}

		@Override
		public void releaseConnection(Connection connection) throws SQLException {
			connectionProvider.closeConnection( connection );
		}

		@Override
		public boolean supportsAggressiveRelease() {
			return connectionProvider.supportsAggressiveRelease();
		}
	}

	public static class MultiTenantConnectionProviderJdbcConnectionAccess implements JdbcConnectionAccess {
		private final MultiTenantConnectionProvider connectionProvider;

		public MultiTenantConnectionProviderJdbcConnectionAccess(MultiTenantConnectionProvider connectionProvider) {
			this.connectionProvider = connectionProvider;
		}

		public MultiTenantConnectionProvider getConnectionProvider() {
			return connectionProvider;
		}

		@Override
		public Connection obtainConnection() throws SQLException {
			return connectionProvider.getAnyConnection();
		}

		@Override
		public void releaseConnection(Connection connection) throws SQLException {
			connectionProvider.releaseAnyConnection( connection );
		}

		@Override
		public boolean supportsAggressiveRelease() {
			return connectionProvider.supportsAggressiveRelease();
		}
	}
}
