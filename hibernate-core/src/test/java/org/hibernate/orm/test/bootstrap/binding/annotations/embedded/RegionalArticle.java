/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id$
package org.hibernate.orm.test.bootstrap.binding.annotations.embedded;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * A regional article is typically a bad design, it keep the country iso2 and a business key as
 * (composite) primary key
 *
 * @author Emmanuel Bernard
 */
@Entity
public class RegionalArticle implements Serializable {
	private RegionalArticlePk pk;
	private String name;

	@Id
	public RegionalArticlePk getPk() {
		return pk;
	}

	public void setPk(RegionalArticlePk pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int hashCode() {
		//a NPE can occurs, but I don't expect hashcode to be used before pk is set
		return getPk().hashCode();
	}

	public boolean equals(Object obj) {
		//a NPE can occurs, but I don't expect equals to be used before pk is set
		if ( obj != null && obj instanceof RegionalArticle ) {
			return getPk().equals( ( (RegionalArticle) obj ).getPk() );
		}
		else {
			return false;
		}
	}
}
