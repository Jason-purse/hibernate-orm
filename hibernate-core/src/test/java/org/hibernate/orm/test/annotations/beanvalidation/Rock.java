/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.orm.test.annotations.beanvalidation;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Rock extends Music {
	@NotNull
	public Integer bit;
}
