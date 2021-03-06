/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.jpa;
import java.util.Date;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * @author Emmanuel Bernard
 */
public class LastUpdateListener {
	@PreUpdate
	@PrePersist
	public void setLastUpdate(Cat o) {
		o.setLastUpdate( new Date() );
		o.setManualVersion( o.getManualVersion() + 1 );
	}
}
