/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.annotations.derivedidentities.e1.b.specjmapid;
import java.io.Serializable;


public class CustomerInventoryPK implements Serializable {

	private Integer id;
	private int custId;

	public CustomerInventoryPK() {
	}

	public CustomerInventoryPK(Integer id, int custId) {
		this.id = id;
		this.custId = custId;
	}

	public boolean equals(Object other) {
		if ( other == this ) {
			return true;
		}
		if ( other == null || getClass() != other.getClass() ) {
			return false;
		}
		CustomerInventoryPK cip = ( CustomerInventoryPK ) other;
		return ( custId == cip.custId && ( id == cip.id ||
				( id != null && id.equals( cip.id ) ) ) );
	}

	public int hashCode() {
		return ( id == null ? 0 : id.hashCode() ) ^ custId;
	}

	public Integer getId() {
		return id;
	}

	public int getCustId() {
		return custId;
	}


}
