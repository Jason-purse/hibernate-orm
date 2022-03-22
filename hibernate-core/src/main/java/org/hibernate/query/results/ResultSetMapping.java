/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.query.results;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.hibernate.Incubating;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.named.NamedResultSetMappingMemento;
import org.hibernate.query.results.dynamic.DynamicFetchBuilderLegacy;
import org.hibernate.sql.results.jdbc.spi.JdbcValuesMappingProducer;

/**
 * Acts as the {@link JdbcValuesMappingProducer} for {@link NativeQuery}
 * or {@link org.hibernate.procedure.ProcedureCall} / {@link jakarta.persistence.StoredProcedureQuery}
 * instances.
 * 表现为 JdbcValuesMappingProducer 对NativeQuery 或者ProcedureCall 或者StoredProcedureQuery的 JdbcValuesMapping的生产者
 *
 * Can be defined<ul>
 *     <li>
 *         statically via {@link jakarta.persistence.SqlResultSetMapping} or `hbm.xml` mapping
 *         // 静态的通过SqlResultSetMapping 或者hbm.xml映射
 *     </li>
 *     <li>
 *         / 动态通过Hibernate-specific API 定义
 *         dynamically via Hibernate-specific APIs:<ul>
 *             <li>{@link NativeQuery#addScalar}</li>
 *             <li>{@link NativeQuery#addEntity}</li>
 *             <li>{@link NativeQuery#addJoin}</li>
 *             <li>{@link NativeQuery#addFetch}</li>
 *             <li>{@link NativeQuery#addRoot}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author Steve Ebersole
 */
@Incubating
public interface ResultSetMapping extends JdbcValuesMappingProducer {
	int getNumberOfResultBuilders();

	void visitResultBuilders(BiConsumer<Integer, ResultBuilder> resultBuilderConsumer);
	void visitLegacyFetchBuilders(Consumer<DynamicFetchBuilderLegacy> resultBuilderConsumer);

	void addResultBuilder(ResultBuilder resultBuilder);
	void addLegacyFetchBuilder(DynamicFetchBuilderLegacy fetchBuilder);

	NamedResultSetMappingMemento toMemento(String name);
}
