/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.connections;

import org.hibernate.Session;
import org.hibernate.dialect.H2Dialect;

import org.hibernate.testing.RequiresDialect;

/**
 * Implementation of CurrentSessionConnectionTest.
 *
 * @author Steve Ebersole
 */
@RequiresDialect(H2Dialect.class)
public class CurrentSessionConnectionTest extends AggressiveReleaseTest {
	@Override
	protected Session getSessionUnderTest() throws Throwable {
		return sessionFactory().getCurrentSession();
	}

	@Override
	protected void release(Session session) {
		// do nothing, txn synch should release session as part of current-session definition
	}
}
