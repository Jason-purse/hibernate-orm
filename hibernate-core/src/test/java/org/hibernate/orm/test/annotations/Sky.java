/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.annotations;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

/**
 * @author Emmanuel Bernard
 */
@Entity
@Table(name = "tbl_sky",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"`month`", "`day`"})}
)
public class Sky implements Serializable {
	@Id
	protected Long id;
	@Column(unique = true, columnDefinition = "varchar(250)", nullable = false)
	protected String color;
	@Column(name="`day`",nullable = false)
	protected String day;
	@Column(name = "`month`", nullable = false)
	protected String month;
	@Transient
	protected String area;
}
