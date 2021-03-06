/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.mapping.naturalid.mutable.cached;

import jakarta.persistence.Entity;

/**
 * @author Guenther Demetz
 */
@Entity
public class SubClass extends AllCached {

	public SubClass() {
		super();
	}
	
	public SubClass(String name) {
		super(name);
	}

}
