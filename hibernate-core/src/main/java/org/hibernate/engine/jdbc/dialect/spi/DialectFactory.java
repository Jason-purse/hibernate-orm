/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.dialect.spi;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.service.Service;

/**
 * A factory for generating Dialect instances.
 *
 * 生成方言实例的工厂 ...
 *
 * @author Steve Ebersole
 */
public interface DialectFactory extends Service {
	/**
	 * Builds an appropriate Dialect instance.
	 *
	 * 构建一个合适的方言实例
	 * <p/>
	 *
	 * 如果一个 方言是显式的在输入属性中键入, 它应该被使用 ... 否则 它根据方言解析器 从连接中 判断方言 ...
	 * If a dialect is explicitly named in the incoming properties, it should used. Otherwise, it is
	 * determined by dialect resolvers based on the passed connection.
	 * <p/>
	 *
	 * 如果方言没有显式的设置并且没有 解析器能够从给定的连接中检测,将抛出异常 ...
	 * An exception is thrown if a dialect was not explicitly set and no resolver could make
	 * the determination from the given connection.
	 *
	 * @param configValues The configuration properties.  用户配置
	 * @param resolutionInfoSource Access to DialectResolutionInfo used to resolve the Dialect to use if not
	 * explicitly named  如果没有显式命名  访问DialectResolutionInfo 来解析方言 ...
	 *
	 * @return The appropriate dialect instance.
	 *
	 * @throws HibernateException No dialect specified and no resolver could make the determination.
	 */
	Dialect buildDialect(Map<String,Object> configValues, DialectResolutionInfoSource resolutionInfoSource) throws HibernateException;
}
