/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.bootstrap.binding.annotations.embedded;
import java.text.NumberFormat;
import jakarta.persistence.Embeddable;

/**
 * Represents fixed part of Interest Rate Swap cash flows.
 */
@Embeddable
public class FixedLeg extends Leg {

	/**
	 * Fixed rate.
	 */
	private double rate;

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String toString() {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits( 4 );
		format.setMaximumFractionDigits( 4 );
		return format.format( getRate() ) + "%";
	}
}
