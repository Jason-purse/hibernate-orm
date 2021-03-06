/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc;

import java.sql.Blob;

/**
 * Contract for {@link Blob} wrappers.
 *
 * @author Steve Ebersole
 */
public interface WrappedBlob {
	/**
	 * Retrieve the wrapped {@link Blob} reference
	 *
	 * @return The wrapped {@link Blob} reference
	 */
	Blob getWrappedBlob();
}
