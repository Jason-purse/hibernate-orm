/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.annotations.embedded.one2many;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;

import jakarta.persistence.Access;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class Name {
	private String first;
	private String last;

	public Name() {
	}

	public Name(String first, String last) {
		this.first = first;
		this.last = last;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}
}
