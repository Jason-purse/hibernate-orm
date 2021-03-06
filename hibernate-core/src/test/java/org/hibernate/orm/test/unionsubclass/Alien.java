/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

//$Id: Alien.java 5686 2005-02-12 07:27:32Z steveebersole $
package org.hibernate.orm.test.unionsubclass;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gavin King
 */
public class Alien extends Being {
	private String species;
	private Hive hive;
	private List hivemates = new ArrayList();
	/**
	 * @return Returns the species.
	 */
	public String getSpecies() {
		return species;
	}
	/**
	 * @param species The species to set.
	 */
	public void setSpecies(String species) {
		this.species = species;
	}
	public Hive getHive() {
		return hive;
	}
	public void setHive(Hive hive) {
		this.hive = hive;
	}
	public List getHivemates() {
		return hivemates;
	}
	public void setHivemates(List hivemates) {
		this.hivemates = hivemates;
	}
}
