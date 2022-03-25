/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.results.jdbc.spi;

import java.util.Set;

import org.hibernate.Incubating;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.results.ResultSetMapping;

/**
 * Producer for JdbcValuesMapping references.
 *	JdbcValuesMapping 引用的生产者
 * The split allows resolution of JDBC value metadata to be used in the
 * production of JdbcValuesMapping references.  Generally this feature is
 * used from {@link ResultSetMapping} instances from native-sql queries and
 * procedure-call queries where not all JDBC types are known and we need the
 * JDBC {@link java.sql.ResultSetMetaData} to determine the types
 * 这允许 JDBC value 元数据解析  - 能够引用在 JDBC ValuesMapping 引用的生产中.. 通常这个特性是从ResultSetMapping 实例 到 native-sql 查询 以及  过程调用查询 - 但是并不是所有的JDBC 类型是已经知道的,我们需要使用JDBC ResultSetMetadata 去判断这些类型
 * @author Steve Ebersole
 */
@Incubating
public interface JdbcValuesMappingProducer {
	/**
	 * Resolve the JdbcValuesMapping.  This involves resolving the
	 * {@link org.hibernate.sql.results.graph.DomainResult} and
	 * {@link org.hibernate.sql.results.graph.Fetch}   解析JdbcValuesMapping -> 这参与到 解析DomainResult 和Fetch
	 */
	JdbcValuesMapping resolve(
			JdbcValuesMetadata jdbcResultsMetadata,
			SessionFactoryImplementor sessionFactory);

	void addAffectedTableNames(Set<String> affectedTableNames, SessionFactoryImplementor sessionFactory);

	default JdbcValuesMappingProducer cacheKeyInstance() {
		return this;
	}
}
