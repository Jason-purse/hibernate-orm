/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.orm.test.bytecode.enhancement.lazy.proxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * @author Steve Ebersole
 */
@Entity(name = "Payment")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Payment {
	private Integer oid;
	private Float amount;

	public Payment() {
	}

	public Payment(Integer oid, Float amount) {
		this.oid = oid;
		this.amount = amount;
	}

	@Id
	@Column(name = "oid")
	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
