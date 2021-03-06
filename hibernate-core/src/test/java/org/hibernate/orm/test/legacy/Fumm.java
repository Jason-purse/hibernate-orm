/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: Fumm.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.orm.test.legacy;
import java.util.Locale;

public class Fumm {
	
	private Locale locale;
	private Fum fum;
	
	public FumCompositeID getId() {
		return fum.getId();
	}
	public void setId(FumCompositeID id) {
	}
	
	public Fumm() {
		super();
	}
	
	/**
	 * Returns the fum.
	 * @return Fum
	 */
	public Fum getFum() {
		return fum;
	}
	
	/**
	 * Returns the locale.
	 * @return Locale
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * Sets the fum.
	 * @param fum The fum to set
	 */
	public void setFum(Fum fum) {
		this.fum = fum;
	}
	
	/**
	 * Sets the locale.
	 * @param locale The locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}






