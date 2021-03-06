/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: E.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.orm.test.legacy;

import java.util.Set;

public class E {
	private Long id;
	private float amount;
	private A reverse;
	private Set as;
	/**
	 * Returns the amount.
	 * @return float
	 */
	public float getAmount() {
		return amount;
	}
	
	/**
	 * Returns the id.
	 * @return long
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the amount.
	 * @param amount The amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	public A getReverse() {
		return reverse;
	}

	public void setReverse(A a) {
		reverse = a;
	}

	/**
	 * @return Returns the as.
	 */
	public Set getAs() {
		return as;
	}

	/**
	 * @param as The as to set.
	 */
	public void setAs(Set as) {
		this.as = as;
	}

}






