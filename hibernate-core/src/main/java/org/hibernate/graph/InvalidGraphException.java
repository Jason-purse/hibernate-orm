/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.graph;

import org.hibernate.HibernateException;

/**
 * Thrown by {@link GraphParser} to indicate textual entity graph representation parsing errors.
 * 
 * @author asusnjar
 *
 */
public class InvalidGraphException extends HibernateException {
	private static final long serialVersionUID = 1L;

	public InvalidGraphException(String message) {
		super( message );
	}
}
