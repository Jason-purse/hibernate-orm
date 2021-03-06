/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.annotations.manytomany;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class BuildingCompany extends Company {
	@Id @GeneratedValue private Long id;
	private Date foundedIn;

	public Date getFoundedIn() {
		return foundedIn;
	}

	public void setFoundedIn(Date foundedIn) {
		this.foundedIn = foundedIn;
	}

	public Long getId() {
		return id;
	}

}
