/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.loader.ast.spi;

/**
 * Common contract for all value-mapping loaders.
 *
 * @author Steve Ebersole
 */
public interface Loader {
	/**
	 * The value-mapping loaded by this loader
	 */
	Loadable getLoadable();
}
