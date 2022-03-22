/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.connections.spi;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides centralized access to JDBC connections.  Centralized to hide the complexity of accounting for contextual
 * (multi-tenant) versus non-contextual access.
 *
 * 提供对JDBC连接的集中访问, 集中隐藏的复杂性占上下文(多租户)和上下文访问。
 *
 * 主要是通过连接提供器工作
 *
 * @author Steve Ebersole
 */
public interface JdbcConnectionAccess extends Serializable {
	/**
	 * Obtain a JDBC connection
	 *
	 * @return The obtained connection
	 *
	 * @throws SQLException Indicates a problem getting the connection
	 */
	Connection obtainConnection() throws SQLException;

	/**
	 * Release a previously obtained connection
	 *
	 * @param connection The connection to release
	 *
	 * @throws SQLException Indicates a problem releasing the connection
	 */
	void releaseConnection(Connection connection) throws SQLException;

	/**
	 * Does the underlying provider of connections support aggressive releasing of connections (and re-acquisition
	 * of those connections later, if need be) in JTA environments?
	 *
	 * @return true/false
	 *
	 * @see ConnectionProvider#supportsAggressiveRelease()
	 * @see MultiTenantConnectionProvider#supportsAggressiveRelease()
	 */
	boolean supportsAggressiveRelease();
}
