/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate;

/**
 * An object capable of determining the entity name for a given entity instance.
 *
 * @see org.hibernate.boot.SessionFactoryBuilder#addEntityNameResolver(EntityNameResolver...)
 *
 * @author Steve Ebersole
 */
@FunctionalInterface
public interface EntityNameResolver {
	/** 根据给定的entity 实例  ,判断它的entity name
	 * Given an entity instance, determine its entity name.
	 *
	 * @param entity The entity instance.
	 *
	 * @return The corresponding entity name, or null if this impl does not know how to perform resolution
	 *         for the given entity instance.
	 */
	String resolveEntityName(Object entity);
}
