/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.community.dialect;

import org.hibernate.dialect.DatabaseVersion;

/**
 * A dialect for the Teradata 14
 *
 * @deprecated use {@code TeradataDialect(14)}
 */
@Deprecated
public class Teradata14Dialect extends TeradataDialect {

	public Teradata14Dialect() {
		super( DatabaseVersion.make( 14 ) );
	}

}
