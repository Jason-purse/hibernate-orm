/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.persister.spi;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.service.Service;

/**
 * Given an entity or collection mapping, resolve the appropriate persister class to use.
 * <p/>
 * The persister class is chosen according to the following rules:<ol>
 *     <li>the persister class defined explicitly via annotation or XML</li>
 *     <li>the persister class returned by the installed {@link PersisterClassResolver}</li>
 *     <li>the default provider as chosen by Hibernate Core (best choice most of the time)</li>
 * </ol>
 *
 * 给定一个Entity 或者集合映射, 解析合适的持久化 类 来 使用
 *
 * 持久化的类   是根据以下规则进行选择
 * 1. 显式的通过注解或者xml 定义
 * 2. 通过PersisterClassResolver 提供的persister class
 * 3. 由Hibernate core 所选择的默认provider(大多数 情况的最佳选择 )
 *
 * @author <a href="mailto:emmanuel@hibernate.org">Emmanuel Bernard</a>
 * @author Steve Ebersole
 */
public interface PersisterClassResolver extends Service {
	/**
	 * Returns the entity persister class for a given entityName or null
	 * if the entity persister class should be the default.
	 *
	 * @param metadata The entity metadata
	 *
	 * @return The entity persister class to use
	 */
	Class<? extends EntityPersister> getEntityPersisterClass(PersistentClass metadata);

	/**
	 * Returns the collection persister class for a given collection role or null
	 * if the collection persister class should be the default.
	 *
	 * @param metadata The collection metadata
	 *
	 * @return The collection persister class to use
	 */
	Class<? extends CollectionPersister> getCollectionPersisterClass(Collection metadata);
}
