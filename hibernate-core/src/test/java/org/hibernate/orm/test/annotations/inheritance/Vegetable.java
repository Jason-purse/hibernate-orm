/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.annotations.inheritance;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * @author Emmanuel Bernard
 */
@Entity()
@Inheritance(
		strategy = InheritanceType.JOINED
)
public class Vegetable {
	private VegetablePk id;
	private long priceInCent;

	@Id
	public VegetablePk getId() {
		return id;
	}

	public void setId(VegetablePk id) {
		this.id = id;
	}

	public long getPriceInCent() {
		return priceInCent;
	}

	public void setPriceInCent(long priceInCent) {
		this.priceInCent = priceInCent;
	}

	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( !( o instanceof Vegetable ) ) return false;

		final Vegetable vegetable = (Vegetable) o;

		if ( !id.equals( vegetable.id ) ) return false;

		return true;
	}

	public int hashCode() {
		return id.hashCode();
	}
}
