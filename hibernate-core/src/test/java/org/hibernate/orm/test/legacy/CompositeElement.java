/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: CompositeElement.java 6844 2005-05-21 14:22:16Z oneovthafew $
package org.hibernate.orm.test.legacy;

import java.io.Serializable;

public class CompositeElement implements Comparable, Serializable {
	private String foo;
	private String bar;
	/**
	 * Returns the bar.
	 * @return String
	 */
	public String getBar() {
		return bar;
	}

	/**
	 * Returns the foo.
	 * @return String
	 */
	public String getFoo() {
		return foo;
	}

	/**
	 * Sets the bar.
	 * @param bar The bar to set
	 */
	public void setBar(String bar) {
		this.bar = bar;
	}

	/**
	 * Sets the foo.
	 * @param foo The foo to set
	 */
	public void setFoo(String foo) {
		this.foo = foo;
	}

	/**
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(Object o) {
		return ( (CompositeElement) o ).foo.compareTo(foo);
	}
	
	public int hashCode() {
		return foo.hashCode() + bar.hashCode();
	}
	
	public boolean equals(Object that) {
		CompositeElement ce = (CompositeElement) that;
		return ce.bar.equals(bar) && ce.foo.equals(foo);
	}

}
