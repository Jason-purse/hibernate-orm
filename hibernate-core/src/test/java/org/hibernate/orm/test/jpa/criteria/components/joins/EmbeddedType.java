/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.orm.test.jpa.criteria.components.joins;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

/**
 * @author Matt Todd
 */
@Embeddable
public class EmbeddedType {
	@ManyToOne(cascade = CascadeType.ALL)
	private ManyToOneType manyToOneType;

	public EmbeddedType() {
	}

	public EmbeddedType(ManyToOneType manyToOneType) {
		this.manyToOneType = manyToOneType;
	}

	public ManyToOneType getManyToOneType() {
		return manyToOneType;
	}

	public void setManyToOneType(ManyToOneType manyToOneType) {
		this.manyToOneType = manyToOneType;
	}
}
