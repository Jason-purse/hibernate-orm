/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.ast.spi;

import org.hibernate.sql.ast.tree.select.QueryPart;

/**
 * SqlAstProcessingState specialization for   sql ast query process state spec
 * @author Steve Ebersole
 */
public interface SqlAstQueryPartProcessingState extends SqlAstProcessingState {
	/**
	 * Get the QueryPart being processed as part of this state.  It is
	 * considered in-flight as it is probably still being built.
	 */
	QueryPart getInflightQueryPart();
}
