/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: Parent.java 4478 2004-09-02 02:30:28Z oneovthafew $
package org.hibernate.orm.test.compositeelement;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author gavin
 */
public class Parent {
	private Long id;
	private String name;
	private Collection children = new HashSet();
	Parent() {}
	public Parent(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the children.
	 */
	public Collection getChildren() {
		return children;
	}
	/**
	 * @param children The children to set.
	 */
	public void setChildren(Collection children) {
		this.children = children;
	}
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
