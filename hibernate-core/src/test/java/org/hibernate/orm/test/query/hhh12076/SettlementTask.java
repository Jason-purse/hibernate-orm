/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.query.hhh12076;

public class SettlementTask extends Task<Settlement> {

	private Settlement _linked;

	public Settlement getLinked() {
		return _linked;
	}

	public void setLinked(Settlement settlement) {
		_linked = settlement;
	}

}
