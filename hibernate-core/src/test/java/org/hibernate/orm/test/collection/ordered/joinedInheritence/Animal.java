/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.collection.ordered.joinedInheritence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author Steve Ebersole
 */
@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public class Animal {
	private Long id;
	private long weight;

	@Id
	@GeneratedValue( generator = "increment" )
	@GenericGenerator( strategy = "increment", name = "increment" )
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}
}
