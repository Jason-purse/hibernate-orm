/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.annotations.cid;

import java.io.Serializable;
import jakarta.persistence.Embeddable;

/**
 * @author bartek
 */
@Embeddable
public class SomeEntityId implements Serializable {
	private Integer id;

	private Integer version;

	@org.hibernate.annotations.Parent
	private SomeEntity parent;

	/**
	 *
	 */
	public SomeEntityId() {
		super();
	}

	public SomeEntityId(int id, int version) {
		super();
		this.id = id;
		this.version = version;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the parent
	 */
	public SomeEntity getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(SomeEntity parent) {
		this.parent = parent;
	}
}
