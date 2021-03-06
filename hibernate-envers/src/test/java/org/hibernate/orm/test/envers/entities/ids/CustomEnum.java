/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.envers.entities.ids;


/**
 * @author Slawek Garwol (slawekgarwol at gmail dot com)
 */
public enum CustomEnum {
	YES,
	NO;

	public String toYesNo() {
		return this == YES ? "Y" : "N";
	}

	public static CustomEnum fromYesNo(String value) {
		return "Y".equals( value ) ? YES : NO;
	}
}
