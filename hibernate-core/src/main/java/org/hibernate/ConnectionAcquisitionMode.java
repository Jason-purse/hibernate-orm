/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate;

import org.hibernate.internal.util.StringHelper;

/**
 * Indicates the manner in which JDBC Connections should be acquired.  Inverse to
 * {@link ConnectionReleaseMode}.
 *
 * @author Steve Ebersole
 */
public enum ConnectionAcquisitionMode {
	/**
	 * The Connection will be acquired as soon as the Hibernate Session is opened.  This
	 * also circumvents ConnectionReleaseMode, as the Connection will then be held until the
	 * Session is closed.
	 *
	 * 立即获取连接 - 这影响了连接释放模式
	 */
	IMMEDIATELY,
	/**
	 * The legacy behavior.  A Connection is only acquired when (if) it is actually needed.
	 * 遗留的 行为 , 当实际需要的时候才获取 ...
	 */
	AS_NEEDED;

	public static ConnectionAcquisitionMode interpret(String value) {
		if ( "immediate".equalsIgnoreCase( value ) || "immediately".equalsIgnoreCase( value ) ) {
			return IMMEDIATELY;
		}

		return AS_NEEDED;
	}

	public static ConnectionAcquisitionMode interpret(Object setting) {
		if ( setting == null ) {
			return null;
		}

		if ( setting instanceof ConnectionAcquisitionMode ) {
			return (ConnectionAcquisitionMode) setting;
		}

		final String value = setting.toString();
		if ( StringHelper.isEmpty( value ) ) {
			return null;
		}

		return interpret( value );
	}
}
