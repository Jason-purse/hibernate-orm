/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.jpa.callbacks;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * @author Emmanuel Bernard
 */
@Entity
@EntityListeners({IncreaseListener.class})
public class Television extends VideoSystem {
	private Integer id;
	private RemoteControl control;
	private String name;

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public RemoteControl getControl() {
		return control;
	}

	public void setControl(RemoteControl control) {
		this.control = control;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@PreUpdate
	public void isLast() {
		if ( isLast ) throw new IllegalStateException();
		isFirst = false;
		isLast = true;
		communication++;
	}

	@PrePersist
	public void prepareEntity() {
		//override a super method annotated with the same
		// event for it not to be called
		counter++;
	}
}
