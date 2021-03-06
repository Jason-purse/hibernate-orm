/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.envers.entities.reventity.trackmodifiedentities;

import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class CustomTrackingRevisionListener implements EntityTrackingRevisionListener {
	@Override
	public void entityChanged(
			Class entityClass, String entityName, Object entityId, RevisionType revisionType,
			Object revisionEntity) {
		((CustomTrackingRevisionEntity) revisionEntity).addModifiedEntityType( entityClass.getName() );
	}

	@Override
	public void newRevision(Object revisionEntity) {
	}
}
