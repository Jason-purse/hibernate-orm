/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.naming.Referenceable;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManagerFactory;

/**
 * 会话工厂代表了  一个Hibernate 的实例
 * 它维护了代表着持久化实体的运行时元数据 模型,它们的属性 ,它们的关联  以及 它们与关系型数据库表的映射
 * configuration 会影响 Hibernate 的运行时行为, 服务的实例 - 需要它们执行它们的职责 ..
 * A {@code SessionFactory} represents an "instance" of Hibernate: it maintains
 * the runtime metamodel representing persistent entities, their attributes,
 * their associations, and their mappings to relational database tables, along
 * with {@linkplain org.hibernate.cfg.AvailableSettings configuration} that
 * affects the runtime behavior of Hibernate, and instances of services that
 * Hibernate needs to perform its duties.
 * <p>
 * 尤为重要的, 一个程序会抓取Session, 通常来说  一个程序有单个 SessionFactory  实例, 并且每一次它服务一个客户端请求 需要从
 * 工厂中包含一个Session 实例 ...
 * Crucially, this is where a program comes to obtain {@link Session sessions}.
 * Typically, a program has a single {@link SessionFactory} instance, and must
 * obtain a new {@link Session} instance from the factory each time it services
 * a client request.
 * <p>
 * 依赖于Hibernate 如何配置, 这个SessionFactory 自身  可能需要对  JDBC 连接池的生命周期 以及事务负责
 * // 或者仅仅简单的作为一个客户端（由容器环境提供的连接池  或者事务管理器)
 * Depending on how Hibernate is configured, the {@code SessionFactory} itself
 * might be responsible for the lifecycle of pooled JDBC connections and
 * transactions, or it may simply act as a client for a connection pool or
 * transaction manager provided by a container environment.
 * <p>
 *     SessionFactory 的内部状态是认为不可变的..
 *     然而 它和有状态服务(例如 JDBC连接池)交互的时候, 例如状态的改变绝不会使得它的客户端可见 ..
 *     尤其是  运行时 元数据 模型   呈现了 entity 以及 它的O/R 映射 是固定的 - 在SessionFactory 创建时即固定 ..
 *     当然SessionFactory 是线程安全的 ...
 * The internal state of a {@code SessionFactory} is considered in some sense
 * "immutable". While it interacts with stateful services like JDBC connection
 * pools, such state changes are never visible to its clients. In particular,
 * the runtime metamodel representing the entities and their O/R mappings is
 * fixed as soon as the {@code SessionFactory} is created. Of course, any
 * {@code SessionFactory} is threadsafe.
 * <p>
 *     每一个SessionFactory 是一个JPA EntityManagerFactory
 * Every {@code SessionFactory} is a JPA {@link EntityManagerFactory}.
 * 因此,Hibernate 可以作为一个JPA 持久化 提供器 ...
 * EntityManagerFactory#unwrap(Class) 也许 可以被用来获取一个底层的SessionFactory
 * Furthermore, when Hibernate is acting as the JPA persistence provider, the
 * method {@link EntityManagerFactory#unwrap(Class)} may be used to obtain the
 * underlying {@code SessionFactory}.
 *
 * @see Session
 * @see org.hibernate.cfg.Configuration
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface SessionFactory extends EntityManagerFactory, Referenceable, Serializable, java.io.Closeable {
	/**
	 * Get the special options used to build the factory.
	 *
	 * @return The special options used to build the factory.
	 */
	SessionFactoryOptions getSessionFactoryOptions();

	/**
	 * Obtain a {@linkplain SessionBuilder session builder} for creating
	 * new {@link Session}s with certain customized options.
	 *
	 * @return The session builder
	 */
	SessionBuilder withOptions();

	/**
	 * Open a {@link Session}.
	 * <p/>
	 * Any JDBC {@link Connection connection} will be obtained lazily from the
	 * {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider}
	 * as needed to perform requested work.
	 *
	 * 打开一个会话
	 * 任何一个JDBC 连接将会从ConnectionProvider中懒加载
	 *
	 * @return The created session.
	 *
	 * @throws HibernateException Indicates a problem opening the session; pretty rare here.
	 */
	Session openSession() throws HibernateException;

	/**
	 * Obtains the <em>current session</em>, an instance of {@link Session}
	 * implicitly associated with some context. For example, the session
	 * might be associated with the current thread, or with the current
	 * JTA transaction.
	 * <p>
	 * The context used for scoping the current session (that is, the
	 * definition of what precisely "current" means here) is determined
	 * by an implementation of
	 * {@link org.hibernate.context.spi.CurrentSessionContext}. An
	 * implementation may be selected using the configuration property
	 * {@value org.hibernate.cfg.AvailableSettings#CURRENT_SESSION_CONTEXT_CLASS}.
	 * <p>
	 * If no {@link org.hibernate.context.spi.CurrentSessionContext} is
	 * explicitly configured, but JTA is configured, then
	 * {@link org.hibernate.context.internal.JTASessionContext} is used.
	 *
	 * @return The current session.
	 *
	 * @throws HibernateException Indicates an issue locating a suitable current session.
	 */
	Session getCurrentSession() throws HibernateException;

	/**
	 * Obtain a {@link StatelessSession} builder.
	 *
	 * @return The stateless session builder
	 */
	StatelessSessionBuilder withStatelessOptions();

	/**
	 * Open a new stateless session.
	 *
	 * @return The created stateless session.
	 */
	StatelessSession openStatelessSession();

	/**
	 * Open a new stateless session, utilizing the specified JDBC
	 * {@link Connection}.
	 *
	 * @param connection Connection provided by the application.
	 *
	 * @return The created stateless session.
	 */
	StatelessSession openStatelessSession(Connection connection);

	/**
	 * Open a Session and perform a action using it
	 */
	default void inSession(Consumer<Session> action) {
		try (Session session = openSession()) {
			action.accept( session );
		}
	}

	/**
	 * Open a {@link Session} and perform an action using the session
	 * within the bounds of a transaction.
	 */
	default void inTransaction(Consumer<Session> action) {
		inSession(
				session -> {
					final Transaction txn = session.beginTransaction();

					try {
						action.accept( session );

						if ( !txn.isActive() ) {
							throw new TransactionManagementException(
									"Execution of action caused managed transaction to be completed" );
						}
					}
					catch (RuntimeException e) {
						// an error happened in the action
						if ( txn.isActive() ) {
							try {
								txn.rollback();
							}
							catch (Exception ignore) {
							}
						}

						throw e;
					}

					// action completed with no errors - attempt to commit the transaction allowing
					// 		any RollbackException to propagate.  Note that when we get here we know the
					//		txn is active

					txn.commit();
				}
		);
	}

	class TransactionManagementException extends RuntimeException {
		TransactionManagementException(String message) {
			super( message );
		}
	}

	/**
	 * Open a Session and perform an action using it.
	 */
	default <R> R fromSession(Function<Session,R> action) {
		try (Session session = openSession()) {
			return action.apply( session );
		}
	}

	/**
	 * Open a {@link Session} and perform an action using the session
	 * within the bounds of a transaction.
	 */
	default <R> R fromTransaction(Function<Session,R> action) {
		return fromSession(
				session -> {
					final Transaction txn = session.beginTransaction();
					try {
						R result = action.apply( session );

						if ( !txn.isActive() ) {
							throw new TransactionManagementException(
									"Execution of action caused managed transaction to be completed" );
						}

						// action completed with no errors - attempt to commit the transaction allowing
						// 		any RollbackException to propagate.  Note that when we get here we know the
						//		txn is active

						txn.commit();

						return result;
					}
					catch (RuntimeException e) {
						// an error happened in the action
						if ( txn.isActive() ) {
							try {
								txn.rollback();
							}
							catch (Exception ignore) {
							}
						}

						throw e;
					}
				}
		);
	}

	/**
	 * Retrieve the {@linkplain Statistics statistics} for this factory.
	 *
	 * @return The statistics.
	 */
	Statistics getStatistics();

	/**
	 * Destroy this {@code SessionFactory} and release all its resources,
	 * including caches and connection pools.
	 * <p/>
	 * It is the responsibility of the application to ensure that there are
	 * no open {@linkplain Session sessions} before calling this method as
	 * the impact on those {@linkplain Session sessions} is indeterminate.
	 * <p/>
	 * No-ops if already {@linkplain #isClosed() closed}.
	 *
	 * @throws HibernateException Indicates an issue closing the factory.
	 */
	void close() throws HibernateException;

	/**
	 * Is this factory already closed?
	 *
	 * @return True if this factory is already closed; false otherwise.
	 */
	boolean isClosed();

	/**
	 * Obtain direct access to the underlying cache regions.
	 *
	 * @return The direct cache access API.
	 */
	@Override
	Cache getCache();

	/**
	 * Return all {@link EntityGraph}s registered for the given entity type.
	 * 
	 * @see #addNamedEntityGraph 
	 */
	<T> List<EntityGraph<? super T>> findEntityGraphsByType(Class<T> entityClass);

	/**
	 * Obtain the set of names of all {@link FilterDefinition defined filters}.
	 *
	 * @return The set of filter names.
	 */
	Set<String> getDefinedFilterNames();

	/**
	 * Obtain the definition of a filter by name.
	 *
	 * @param filterName The name of the filter for which to obtain the definition.
	 * @return The filter definition.
	 * @throws HibernateException If no filter defined with the given name.
	 */
	FilterDefinition getFilterDefinition(String filterName) throws HibernateException;

	/**
	 * Determine if this session factory contains a fetch profile definition
	 * registered under the given name.
	 *
	 * @param name The name to check
	 * @return True if there is such a fetch profile; false otherwise.
	 */
	boolean containsFetchProfileDefinition(String name);

}
