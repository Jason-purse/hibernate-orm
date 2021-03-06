/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.jpa.lock;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.Version;

/**
 * @author Emmanuel Bernard
 */
@Entity(name="Lock_")
@NamedQuery(
		name="AllLocks",
		query="from Lock_",
		lockMode = LockModeType.PESSIMISTIC_WRITE,
		hints = { @QueryHint( name = "javax.persistence.lock.timeout", value = "0")}
)
public class Lock {
	private Integer id;
	private Integer version;
	private String name;

	public Lock() {
	}

	public Lock(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
