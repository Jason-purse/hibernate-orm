/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.jpa.cascade2;


/**
 * todo: describe ChildInfo
 *
 * @author Steve Ebersole
 */
public class ParentInfo {
	private Long id;
	private Parent owner;
	private String info;

	public ParentInfo() {
	}

	public ParentInfo(String info) {
		this.info = info;
	}

	public Long getId() {
		return id;
	}

	public Parent getOwner() {
		return owner;
	}

	public void setOwner(Parent owner) {
		this.owner = owner;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
