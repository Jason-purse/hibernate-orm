/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: MilitaryBuilding.java 14760 2008-06-11 07:33:15Z hardy.ferentschik $
package org.hibernate.orm.test.annotations.id.sequences.entities;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;

/**
 * @author Emmanuel Bernard
 */
@MappedSuperclass
@IdClass(Location.class)
public class MilitaryBuilding {
	@Id
	public double longitude;
	@Id
	public double latitude;
}
