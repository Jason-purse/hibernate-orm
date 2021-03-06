/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.loader.ast.spi;

import org.hibernate.LockOptions;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * Loader for loading an entity by a single identifier value.
 *
 * 通过单个identifier value 获取一个entity 的加载器
 *
 * @author Steve Ebersole
 */
public interface SingleIdEntityLoader<T> extends SingleEntityLoader<T> {
	/**
	 * Load by primary key value
	 */
	@Override
	T load(Object pkValue, LockOptions lockOptions, Boolean readOnly, SharedSessionContractImplementor session);

	/**
	 * Load by primary key value, populating the passed entity instance.  Used to initialize an uninitialized
	 * bytecode-proxy or {@link org.hibernate.event.spi.LoadEvent} handling.
	 * The passed instance is the enhanced proxy or the entity to be loaded.
	 */
	T load(Object pkValue, Object entityInstance, LockOptions lockOptions, Boolean readOnly, SharedSessionContractImplementor session);

	default T load(Object pkValue, Object entityInstance, LockOptions lockOptions, SharedSessionContractImplementor session) {
		return load( pkValue, entityInstance, lockOptions, null, session );
	}

	/**
	 * Load database snapshot by primary key value
	 * 通过主键加载数据库快照
	 */
	Object[] loadDatabaseSnapshot(Object id, SharedSessionContractImplementor session);
}
