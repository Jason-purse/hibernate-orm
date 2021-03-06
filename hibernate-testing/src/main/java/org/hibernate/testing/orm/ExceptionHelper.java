/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.testing.orm;

/**
 * @author Steve Ebersole
 */
public class ExceptionHelper {
	public static Throwable getRootCause(Throwable error) {
		Throwable toProcess = error;
		while ( toProcess.getCause() != null ) {
			toProcess = toProcess.getCause();
		}
		return toProcess;
	}
}
