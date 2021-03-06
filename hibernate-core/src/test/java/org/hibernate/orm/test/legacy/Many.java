/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: Many.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.orm.test.legacy;


public class Many {
	Long key;
	One one;
	private int x;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public void setKey(Long key) {
		this.key = key;
	}
	
	public Long getKey() {
		return this.key;
	}
	
	public void setOne(One one) {
		this.one = one;
	}
	
	public One getOne() {
		return this.one;
	}
}






