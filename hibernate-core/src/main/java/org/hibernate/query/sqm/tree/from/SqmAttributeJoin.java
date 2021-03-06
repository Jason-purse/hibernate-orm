/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.query.sqm.tree.from;

import org.hibernate.HibernateException;
import org.hibernate.metamodel.model.domain.EntityDomainType;
import org.hibernate.query.criteria.JpaFetch;
import org.hibernate.query.criteria.JpaJoin;
import org.hibernate.query.sqm.SqmPathSource;
import org.hibernate.query.hql.spi.SqmCreationProcessingState;
import org.hibernate.query.sqm.tree.domain.SqmPath;
import org.hibernate.query.sqm.tree.predicate.SqmPredicate;
import org.hibernate.type.descriptor.java.JavaType;

/**
 * Models a join based on a mapped attribute reference.
 *
 * @author Steve Ebersole
 */
public interface SqmAttributeJoin<O,T> extends SqmQualifiedJoin<O,T>, JpaFetch<O,T>, JpaJoin<O,T> {
	@Override
	SqmFrom<?,O> getLhs();

	@Override
	SqmPathSource<T> getReferencedPathSource();

	@Override
	JavaType<T> getJavaTypeDescriptor();

	boolean isFetched();

	@Override
	SqmPredicate getJoinPredicate();

	void setJoinPredicate(SqmPredicate predicate);

	@Override
	<S extends T> SqmAttributeJoin<O, S> treatAs(Class<S> treatJavaType);

	@Override
	<S extends T> SqmAttributeJoin<O, S> treatAs(EntityDomainType<S> treatTarget);

	SqmAttributeJoin makeCopy(SqmCreationProcessingState creationProcessingState);

	class NotJoinableException extends HibernateException {
		public NotJoinableException(String message) {
			super( message );
		}

		public NotJoinableException(String message, Throwable cause) {
			super( message, cause );
		}
	}
}
