/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.annotations.manytomany;
import java.io.Serializable;
import jakarta.persistence.Column;

/**
 * @author Emmanuel Bernard
 */
public class CatPk implements Serializable {
	private String name;
	private String thoroughbred;

	@Column(length=128)
	public String getThoroughbred() {
		return thoroughbred;
	}

	public void setThoroughbred(String thoroughbred) {
		this.thoroughbred = thoroughbred;
	}

	@Column(length=128)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( !( o instanceof CatPk ) ) return false;

		final CatPk catPk = (CatPk) o;

		if ( !name.equals( catPk.name ) ) return false;
		if ( !thoroughbred.equals( catPk.thoroughbred ) ) return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = name.hashCode();
		result = 29 * result + thoroughbred.hashCode();
		return result;
	}
}
