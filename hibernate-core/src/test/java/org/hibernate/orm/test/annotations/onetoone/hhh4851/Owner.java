/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.annotations.onetoone.hhh4851;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table
public class Owner {

	private boolean deleted = false;
	private Long id;

	private String name;
	private Integer version;

	public Owner() {

	}

	public Owner(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public Owner setDeleted(boolean isDeleted) {
		this.deleted = isDeleted;
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Owner setName(String name) {
		this.name = name;
		return this;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
