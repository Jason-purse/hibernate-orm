/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.orm.post;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;

/**
 * @author Steve Ebersole
 *
 * 项目的插件
 */
public class ReportGenerationPlugin implements Plugin<Project> {
	// 配置名
	public static final String CONFIG_NAME = "reportAggregation";
	// 任务分组
	public static final String TASK_GROUP_NAME = "hibernate-reports";

	@Override
	public void apply(Project project) {
		final Configuration artifactsToProcess = project.getConfigurations()
				.maybeCreate( CONFIG_NAME )
				.setDescription( "Used to collect the jars with classes files to be used in the aggregation reports for `@Internal`, `@Incubating`, etc" );

		// 获取这个任务,设置 任务分组名
		final Task groupingTask = project.getTasks().maybeCreate( "generateHibernateReports" );
		groupingTask.setGroup( TASK_GROUP_NAME );

		// 这个索引管理器  是hibernate 封装了 java annotation indexer
		// 传入配置(也相当于传入了资源集),以及当前项目
		final IndexManager indexManager = new IndexManager( artifactsToProcess, project );
		// 然后创建一个任务
		final IndexerTask indexerTask = project.getTasks().create(
				"buildAggregatedIndex",
				IndexerTask.class,
				indexManager
		);
		// 依赖关系设置
		groupingTask.dependsOn( indexerTask );

		// incubationReport 任务生成
		final IncubationReportTask incubatingTask = project.getTasks().create(
				"generateIncubationReport",
				IncubationReportTask.class,
				indexManager
		);
		// 依赖关系设置
		incubatingTask.dependsOn( indexerTask );
		// 依赖关系设置
		groupingTask.dependsOn( incubatingTask );

		// internal report 任务生成
		final InternalsReportTask internalsTask = project.getTasks().create(
				"generateInternalsReport",
				InternalsReportTask.class,
				indexManager
		);
		// 设置依赖关系
		internalsTask.dependsOn( indexerTask );
		groupingTask.dependsOn( internalsTask );

		// 日志任务
		final LoggingReportTask loggingTask = project.getTasks()
						.create("generateLoggingReport",
								LoggingReportTask.class,
								indexManager);
		// 依赖关系设置
		loggingTask.dependsOn( indexerTask );
		groupingTask.dependsOn( loggingTask );
	}
}
