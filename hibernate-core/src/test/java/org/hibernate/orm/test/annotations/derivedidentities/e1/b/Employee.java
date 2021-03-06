/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.annotations.derivedidentities.e1.b;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Employee {
	@Id
	long empId;
	String empName;
}
