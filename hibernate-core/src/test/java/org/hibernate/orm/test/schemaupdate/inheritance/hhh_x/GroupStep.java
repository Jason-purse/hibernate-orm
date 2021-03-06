/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.schemaupdate.inheritance.hhh_x;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

/**
 * @author Andrea Boriero
 */
@DiscriminatorValue("group")
@Entity
public class GroupStep extends Step {

	@OneToMany(mappedBy = "parent")
	private List<Step> steps;
}
