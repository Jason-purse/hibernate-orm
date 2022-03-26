/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.action.spi;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

/**
 * An operation which may be scheduled for later execution.  Usually, the operation is a database
 * insert/update/delete, together with required second-level cache management.
 * 	一个操作(后面调度执行的一个操作), 通常来说, 操作是要给数据库的insert / update / delete / 并结合了一些必要的二级缓存管理 ..
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface Executable {
	/**
	 * What spaces (tables) are affected by this action?
	 * 那些表被此动作影响了 ...
	 *
	 * @return The spaces affected by this action.
	 */
	Serializable[] getPropertySpaces();

	/**
	 * Called before executing any actions.  Gives actions a chance to perform any preparation.
	 *	执行任何动作之前调用, 在执行任何操作之前给动作一个机会..
	 * @throws HibernateException Indicates a problem during preparation.
	 */
	void beforeExecutions() throws HibernateException;

	/**
	 * Execute this action.
	 * 执行这个动作
	 * @throws HibernateException Indicates a problem during execution.
	 */
	void execute() throws HibernateException;

	/**
	 * Get the after-transaction-completion process, if any, for this action.
	 *	如果有, 对这个动作  - 进行 事务完成之后的处理 ...
	 * @return The after-transaction-completion process, or null if we have no
	 * after-transaction-completion process
	 */
	AfterTransactionCompletionProcess getAfterTransactionCompletionProcess();

	/**
	 * Get the before-transaction-completion process, if any, for this action.
	 *	如果有,对于此动作, 进行事务完成前的处理
	 * @return The before-transaction-completion process, or null if we have no
	 * before-transaction-completion process
	 */
	BeforeTransactionCompletionProcess getBeforeTransactionCompletionProcess();
	
	/**	反序列化之后的session 重连 ...
	 * Reconnect to session after deserialization
	 *
	 * @param session The session being deserialized
	 */
	void afterDeserialize(SharedSessionContractImplementor session);
}
