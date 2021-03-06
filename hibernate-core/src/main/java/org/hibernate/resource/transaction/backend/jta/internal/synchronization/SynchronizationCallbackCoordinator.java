/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.resource.transaction.backend.jta.internal.synchronization;

import jakarta.transaction.Synchronization;

/**
 * Manages funneling JTA Synchronization callbacks back into the Hibernate transaction engine.
 *
 * @author Steve Ebersole
 */
public interface SynchronizationCallbackCoordinator extends Synchronization {
	/**
	 * Called by the TransactionCoordinator when it registers the Synchronization with the JTA system
	 */
	void synchronizationRegistered();

	/**
	 * Called by the TransactionCoordinator to allow the SynchronizationCallbackCoordinator to process any
	 * after-completion handling that it may have delayed due to thread affinity
	 *
	 * 由TransactionCoordinator 调用去允许SynchronizationCallbackCoordinator 处理任何一个完成之后的处理(它也许由于线程的关联需要延时)
	 */
	void processAnyDelayedAfterCompletion();
}
