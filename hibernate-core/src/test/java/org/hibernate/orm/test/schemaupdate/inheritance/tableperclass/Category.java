/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.schemaupdate.inheritance.tableperclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * @author Andrea Boriero
 */
@Entity
@Table(name = "CATEGORY")
public class Category extends Element {
}
