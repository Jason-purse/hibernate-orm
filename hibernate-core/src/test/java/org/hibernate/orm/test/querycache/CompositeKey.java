/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.querycache;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class CompositeKey implements Serializable {

	private static final long serialVersionUID = 7950910288405475131L;

	public int a;

	public int b;

	public CompositeKey() {
	}
	
	public CompositeKey(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + b;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKey other = (CompositeKey) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		return true;
	}

}
