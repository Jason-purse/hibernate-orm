/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.jaxb.mapping.internal;

import jakarta.persistence.FetchType;

/**
 * Marshalling support for dealing with JPA FetchType enums.  Plugged into JAXB for binding
 *
 * @author Steve Ebersole
 */
public class FetchTypeMarshalling {
	public static FetchType fromXml(String name) {
		return FetchType.valueOf( name );
	}

	public static String toXml(FetchType fetchType) {
		return fetchType.name();
	}
}
