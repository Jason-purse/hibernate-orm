/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.jpa.ops;


/**
 * @author Gavin King
 */
public class NumberedNode extends Node {

	private long id;

	public NumberedNode() {
		super();
	}


	public NumberedNode(String name) {
		super( name );
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
