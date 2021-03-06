/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: FumCompositeID.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.orm.test.legacy;



public class FumCompositeID implements java.io.Serializable {
	String string_;
	short short_;
	public boolean equals(Object other) {
		FumCompositeID that = (FumCompositeID) other;
		return this.string_.equals(that.string_) && this.short_==that.short_;
	}
	public int hashCode() {
		return string_.hashCode();
	}
	public String getString() {
		return string_;
	}
	public void setString(String string_) {
		this.string_ = string_;
	}
	public short getShort() {
		return short_;
	}
	public void setShort(short short_) {
		this.short_ = short_;
	}
}







