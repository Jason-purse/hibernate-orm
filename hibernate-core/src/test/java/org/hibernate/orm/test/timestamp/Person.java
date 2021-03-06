/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: Person.java 8054 2005-08-31 20:12:24Z oneovthafew $
package org.hibernate.orm.test.timestamp;
import java.util.Date;

/**
 * @author Gavin King
 */
public class Person {
	private String name;
	private Date dob;
	private String currentAddress;
	Person() {}
	public Person(String name, Date dob, String address) {
		this.name = name;
		this.dob = dob;
		this.currentAddress = address;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrentAddress() {
		return currentAddress;
	}
	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
}
