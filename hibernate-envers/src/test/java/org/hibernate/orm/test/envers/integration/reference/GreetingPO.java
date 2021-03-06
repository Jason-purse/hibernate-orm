/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.envers.integration.reference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class GreetingPO {
	@Id
	@GeneratedValue
	private Long id;

	private String theGreeting;

	@ManyToOne
	private GreetingSetPO greetingSet;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGreeting() {
		return theGreeting;
	}

	public void setGreeting(String greeting) {
		this.theGreeting = greeting;
	}

	public GreetingSetPO getGreetingSet() {
		return greetingSet;
	}

	public void setGreetingSet(GreetingSetPO greetingSet) {
		this.greetingSet = greetingSet;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof GreetingPO) ) {
			return false;
		}

		GreetingPO that = (GreetingPO) o;

		if ( id != null ? !id.equals( that.id ) : that.id != null ) {
			return false;
		}
		if ( theGreeting != null ? !theGreeting.equals( that.theGreeting ) : that.theGreeting != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (theGreeting != null ? theGreeting.hashCode() : 0);
		return result;
	}
}
