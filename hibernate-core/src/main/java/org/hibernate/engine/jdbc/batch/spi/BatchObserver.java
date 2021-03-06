/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.batch.spi;


/**
 * An observer contract for batch events.
 * batch 事件 的监听器
 * @author Steve Ebersole
 */
public interface BatchObserver {
	/**
	 * Indicates explicit execution of the batch via a call to {@link Batch#execute()}.
	 * 通过Batch#execute  指示一个batch 的显式执行 ..
	 */
	void batchExplicitlyExecuted();

	/**
	 * Indicates an implicit execution of the batch.
	 */
	void batchImplicitlyExecuted();
}
