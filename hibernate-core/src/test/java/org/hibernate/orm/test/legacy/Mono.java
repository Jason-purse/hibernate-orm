/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: Mono.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.orm.test.legacy;
import java.util.Set;


public class Mono extends Top {
	
	private Set strings;

	/**
	 * Constructor for Mono.
	 * @param c
	 */
	public Mono(int c) {
		super(c);
	}

	/**
	 * Constructor for Mono.
	 */
	public Mono() {
		super();
	}

	/**
	 * Returns the strings.
	 * @return Set
	 */
	public Set getStrings() {
		return strings;
	}

	/**
	 * Sets the strings.
	 * @param strings The strings to set
	 */
	public void setStrings(Set strings) {
		this.strings = strings;
	}

}
