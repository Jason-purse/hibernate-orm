/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.jpa.emops;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public class Dog extends Pet {
	private int numBones;

	public int getNumBones() {
		return numBones;
	}

	public void setNumBones(int numBones) {
		this.numBones = numBones;
	}
}
