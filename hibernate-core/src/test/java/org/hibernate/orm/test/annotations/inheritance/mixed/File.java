/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.annotations.inheritance.mixed;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;

/**
 * @author Emmanuel Bernard
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "FileMixed")
@SecondaryTable(name = "FileFolderMixed")
@DiscriminatorColumn(length = 1)
public abstract class File {
	private String name;
	private Folder parent;

	File() {
	}

	public File(String name) {
		this.name = name;
	}

	@Id
	public String getName() {
		return name;
	}

	public void setName(String id) {
		this.name = id;
	}

	@ManyToOne
	@JoinColumn(table = "FileFolderMixed")
	public Folder getParent() {
		return parent;
	}

	public void setParent(Folder parent) {
		this.parent = parent;
	}

}
