/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.jpa.metadata;
import jakarta.persistence.MappedSuperclass;

/**
 * @author Emmanuel Bernard
 */
@MappedSuperclass
public class Cattish extends Feline {
	private long hoursOfSleep;

	public long getHoursOfSleep() {
		return hoursOfSleep;
	}

	public void setHoursOfSleep(long hoursOfSleep) {
		this.hoursOfSleep = hoursOfSleep;
	}
}
