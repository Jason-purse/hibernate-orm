/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.registry.selector.spi;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.hibernate.boot.registry.selector.internal.LazyServiceResolver;
import org.hibernate.dialect.Dialect;
import org.hibernate.service.Service;

/**
 * Service which acts as a registry for named strategy implementations.
 *
 * 此服务作为一个注册机 - 已命名的策略实现 ...
 * <p/>
 * Strategies are more open ended than services, though a strategy managed here might very well also be a service.  The
 * strategy is any interface that has multiple, (possibly short) named implementations.
 *
 * 策略比  服务更加 开放 .... 尽管这里使用管理的策略  可能比服务更好,  但是策略可以是任意的一个接口(具有多个 / 甚至更简单的命名实现）
 * <p/>
 * StrategySelector manages resolution of particular implementation by (possibly short) name via the
 * {@link #selectStrategyImplementor} method, which is the main contract here.  As indicated in the docs of that
 * method the given name might be either a short registered name or the implementation FQN.  As an example, consider
 * resolving the {@link org.hibernate.resource.transaction.spi.TransactionCoordinatorBuilder} implementation to use.  To use the
 * JDBC-based TransactionCoordinatorBuilder the passed name might be either {@code "jdbc"} or
 * {@code "org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorBuilderImpl"} (which is the FQN).
 *
 * 策略选择器通过名称 调用selectStrategyImplementor 方法去管理任何一个特殊实现的解析 ...  这里是一个主要的约定 ..
 * 当这个给定的名字可以是一个简单的注册名  或者实现FQN ...(fully qualified name)
 *  例如:  考虑解析TransactionCoordinatorBuilder 实现去使用 ...
 *  为了使用基于JDBC的TransactionCoordinatorBuilder - >传递的名称可能是Jdbc /  org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorBuilderImpl
 *  // 这就是FQN
 * <p/>
 * Strategy implementations can be managed by {@link #registerStrategyImplementor} and
 * {@link #unRegisterStrategyImplementor}.  Originally designed to help the OSGi use case, though no longer used there.
 * <p/>
 * The service also exposes a general typing API via {@link #resolveStrategy} and {@link #resolveDefaultableStrategy}
 * which accept implementation references rather than implementation names, allowing for a multitude of interpretations
 * of said "implementation reference".  See the docs for {@link #resolveDefaultableStrategy} for details.
 *
 * 策略实现能够被registerStrategyImplementor 以及 unRegisterStrategyImplementor 所管理 ...
 * 最初旨在帮助OSGi的用例,虽然不再使用
 *
 * 此服务也暴露了一个通用的典型API - resolveStrategy 以及 resolveDefaultableStrategy
 * 这接受一个实现引用而不是实现参考 , 允许大量的解释说明    叫做: 实现参考 ...
 *
 * @author Christian Beikov
 *
 *
 * 方言选择器
 */
public interface DialectSelector extends Service, LazyServiceResolver<Dialect> {
}
