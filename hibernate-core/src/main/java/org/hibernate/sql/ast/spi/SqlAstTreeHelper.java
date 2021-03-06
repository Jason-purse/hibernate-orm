/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast.spi;

import org.hibernate.sql.ast.tree.predicate.Junction;
import org.hibernate.sql.ast.tree.predicate.Predicate;

/**
 * 抽象语法树帮助器
 * @author Steve Ebersole
 */
public class SqlAstTreeHelper {
	/**
	 * Singleton access
	 */
	public static final SqlAstTreeHelper INSTANCE = new SqlAstTreeHelper();

	private SqlAstTreeHelper() {
	}


	public static Predicate combinePredicates(Predicate baseRestriction, Predicate incomingRestriction) {
		if ( baseRestriction == null ) {
			return incomingRestriction;
		}

		if ( incomingRestriction == null ) {
			return baseRestriction;
		}

		final Junction combinedPredicate;
		// 它本身就是一个Junction 不是一个简单的Predicate
		if ( baseRestriction instanceof Junction ) {
			final Junction junction = (Junction) baseRestriction;
			if ( junction.isEmpty() ) {
				return incomingRestriction;
			}
			// 判断是不是 ANT
			if ( junction.getNature() == Junction.Nature.CONJUNCTION ) {
				combinedPredicate = junction;
			}
			else {
				// 否则 new Junction
				combinedPredicate = new Junction( Junction.Nature.CONJUNCTION );
				combinedPredicate.add( baseRestriction );
			}
		}
		else {
			// 否则同样新加一个
			combinedPredicate = new Junction( Junction.Nature.CONJUNCTION );
			combinedPredicate.add( baseRestriction );
		}

		combinedPredicate.add( incomingRestriction );

		return combinedPredicate;
	}
}
