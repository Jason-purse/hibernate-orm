/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.tool.schema.spi;

import java.util.Map;

import org.hibernate.Incubating;
import org.hibernate.service.Service;
import org.hibernate.tool.schema.internal.exec.GenerationTarget;

/**
 * Contract for schema management tool integration.
 * schema  管理 工具集成的约定
 *
 * @author Steve Ebersole
 */
@Incubating
public interface SchemaManagementTool extends Service {

	SchemaCreator getSchemaCreator(Map<String,Object> options);
	SchemaDropper getSchemaDropper(Map<String,Object> options);
	SchemaMigrator getSchemaMigrator(Map<String,Object> options);
	SchemaValidator getSchemaValidator(Map<String,Object> options);

	/**
	 * This allows to set an alternative implementation for the Database
	 * generation target.
	 * Used by Hibernate Reactive so that it can use the reactive database
	 * access rather than needing a JDBC connection.
	 *
	 * 能够自定义数据库生成目标替换   这里 提供了一个选择  使用响应式  而不是JDBC 连接 ..
	 * @param generationTarget the custom instance to use.
	 */
	void setCustomDatabaseGenerationTarget(GenerationTarget generationTarget);

	ExtractionTool getExtractionTool();
}
