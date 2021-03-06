/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.annotations.bytecode;


/**
 * @author Emmanuel Bernard
 */
public interface Tool {
	public Long getId();

	public void setId(Long id);

	public Number usage();
}
