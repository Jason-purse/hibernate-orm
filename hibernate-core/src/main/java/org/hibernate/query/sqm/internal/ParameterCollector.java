/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.query.sqm.internal;

import org.hibernate.query.sqm.tree.expression.SqmParameter;

/**
 * todo (6.0) : how is this different from {@link org.hibernate.query.sqm.tree.jpa.ParameterCollector}?
 *
 * @author Steve Ebersole
 */
public interface ParameterCollector {
	void addParameter(SqmParameter<?> parameter);
}
