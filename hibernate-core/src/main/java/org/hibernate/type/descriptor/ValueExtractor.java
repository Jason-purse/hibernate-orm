/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.type.descriptor;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contract for extracting value via JDBC from {@link ResultSet} or as output
 * param from {@link CallableStatement}.
 *	从JDBC中抓取数据  或者从CallableStatement 中抓取
 * @author Steve Ebersole
 */
public interface ValueExtractor<X> {
	/**
	 * Extract value from result set
	 *  从result set 中抓取值..
	 * @throws SQLException Indicates a JDBC error occurred.
	 */
	X extract(ResultSet rs, int paramIndex, WrapperOptions options) throws SQLException;

	/**
	 * Extract value from a callable output parameter by index
	 *
	 * @throws SQLException Indicates a JDBC error occurred.
	 */
	X extract(CallableStatement statement, int paramIndex, WrapperOptions options) throws SQLException;

	/**
	 * Extract value from a callable output parameter by name
	 *
	 * @throws SQLException Indicates a JDBC error occurred.
	 */
	X extract(CallableStatement statement, String paramName, WrapperOptions options) throws SQLException;
}
