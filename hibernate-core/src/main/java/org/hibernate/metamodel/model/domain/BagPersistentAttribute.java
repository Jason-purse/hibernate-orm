/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.model.domain;

import java.util.Collection;
import jakarta.persistence.metamodel.CollectionAttribute;

/**
 * Hibernate extension to the JPA {@link CollectionAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface BagPersistentAttribute<D,E>
		extends CollectionAttribute<D,E>, PluralPersistentAttribute<D,Collection<E>,E> {
	@Override
	SimpleDomainType<E> getValueGraphType();

	@Override
	SimpleDomainType<E> getElementType();

	@Override
	ManagedDomainType<D> getDeclaringType();
}
