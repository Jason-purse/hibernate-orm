/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.orphan.one2one.fk.reversed.bidirectional.multilevelcascade;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Tranche {

    @Id
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Tranchenmodell tranchenmodell;


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
}
