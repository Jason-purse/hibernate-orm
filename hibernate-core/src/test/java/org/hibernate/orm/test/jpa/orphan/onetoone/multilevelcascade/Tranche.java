/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.jpa.orphan.onetoone.multilevelcascade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Tranche {

    @Id
	@GeneratedValue
	private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Tranchenmodell tranchenmodell;

	@OneToOne(mappedBy = "tranche", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Y y;

	public Long getId() {
        return id;
    }

	public void setId(Long id) {
		this.id = id;
	}

    public Tranchenmodell getTranchenmodell() {
        return tranchenmodell;
    }

    public void setTranchenmodell(Tranchenmodell tranchenmodell) {
        this.tranchenmodell = tranchenmodell;
    }

	public Y getY() {
		return y;
	}

	public void setY(Y y) {
		this.y = y;
	}
}
