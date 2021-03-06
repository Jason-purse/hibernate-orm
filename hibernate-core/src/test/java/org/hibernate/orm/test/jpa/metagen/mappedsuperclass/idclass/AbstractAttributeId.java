/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.jpa.metagen.mappedsuperclass.idclass;

import java.io.Serializable;
import jakarta.persistence.MappedSuperclass;


/**
 * @author Alexis Bataille
 * @author Steve Ebersole
 */
@MappedSuperclass
public abstract class AbstractAttributeId implements Serializable {
	protected String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
