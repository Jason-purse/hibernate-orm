/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.query.spi;

import org.hibernate.query.BindableType;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * A resolver for Type based on a parameter value being bound, when no
 * explicit type information is supplied.
 *
 * @author Steve Ebersole
 */
public interface QueryParameterBindingTypeResolver {
	<T> BindableType<? extends T> resolveParameterBindType(T bindValue);
	<T> BindableType<T> resolveParameterBindType(Class<T> clazz);
	TypeConfiguration getTypeConfiguration();
}
