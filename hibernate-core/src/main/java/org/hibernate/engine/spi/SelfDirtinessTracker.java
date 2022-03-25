/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.spi;

import org.hibernate.bytecode.enhance.spi.CollectionTracker;

/** 合同一个实体报告说它追踪自己的污秽的状态,而不是需要Hibernate执行state-diff肮脏的计算。 p
 * Contract for an entity to report that it tracks the dirtiness of its own state,
 * as opposed to needing Hibernate to perform state-diff dirty calculations.
 * <p/>
 * Entity classes are free to implement this contract themselves.  This contract is
 * also introduced into the entity when using bytecode enhancement and requesting
 * that entities track their own dirtiness.
 * >实体类可以实现本合同本身。本合同也引入实体使用字节码增强时,要求实体跟踪自己的污秽。
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public interface SelfDirtinessTracker {
	/**
	 * Have any of the entity's persistent attributes changed?
	 *
	 * @return {@code true} indicates one or more persistent attributes have changed; {@code false}
	 * indicates none have changed.
	 */
	boolean $$_hibernate_hasDirtyAttributes();

	/**
	 * Retrieve the names of all the persistent attributes whose values have changed.
	 *
	 * @return An array of changed persistent attribute names
	 */
	String[] $$_hibernate_getDirtyAttributes();

	/**
	 * Adds persistent attribute to the set of values that have changed
	 */
	void $$_hibernate_trackChange(String attributes);

	/**
	 * Clear the stored dirty attributes
	 */
	void $$_hibernate_clearDirtyAttributes();

	/**
	 * Temporarily enable / disable dirty tracking
	 */
	void $$_hibernate_suspendDirtyTracking(boolean suspend);

	/**
	 * Get access to the CollectionTracker
	 */
	CollectionTracker $$_hibernate_getCollectionTracker();
}
