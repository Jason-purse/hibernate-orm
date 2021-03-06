/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.annotations.onetomany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Politician {
	private String name;
	private PoliticalParty party;

	@Id
	@Column(columnDefinition = "VARCHAR(30)")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "party_fk")
	public PoliticalParty getParty() {
		return party;
	}

	public void setParty(PoliticalParty party) {
		this.party = party;
	}
}
