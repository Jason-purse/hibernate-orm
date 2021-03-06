/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.metamodel.wildcardmodel;

import java.util.List;
import jakarta.persistence.Entity;

@Entity
public class OwnerOne extends AbstractOwner {

	@SuppressWarnings("unchecked")
	@Override
	public List<EntityOne> getEntities() {
		return (List<EntityOne>) super.getEntities();
	}
}
