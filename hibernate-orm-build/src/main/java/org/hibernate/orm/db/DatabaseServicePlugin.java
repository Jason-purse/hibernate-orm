/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.orm.db;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.services.BuildServiceRegistry;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.testing.Test;

import static org.hibernate.orm.db.DatabaseService.REGISTRATION_NAME;

/**
 * @author Steve Ebersole
 *
 * 仅仅为了排他性  数据库服务测试
 * // 并提供了一个共享服务 ..注入
 */
public class DatabaseServicePlugin implements Plugin<Project> {
	@Override
	@SuppressWarnings("UnstableApiUsage")
	public void apply(Project project) {
		// register the service used to restrict parallel execution
		// of tests - used to avoid database schema/catalog collisions

		// 用于测试注册这个资源 - 防止并发执行 (限制)
		// 主要目的就是为了避免数据库 schema / catalog 的冲突..
		// 属性注入 - 拿到当前gradle 的共享服务
		final BuildServiceRegistry sharedServices = project.getGradle().getSharedServices();
		// 然后注册一个...
		final Provider<DatabaseService> databaseServiceProvider = sharedServices.registerIfAbsent(
				REGISTRATION_NAME,
				DatabaseService.class,
				spec -> spec.getMaxParallelUsages().set( 1 )
		);

		project.getTasks().withType(Test.class).forEach(
				// BuildServiceRegistry的这个注册方法返回一个Provider 可以用来连接服务和任务
				(Test test) -> {
					test.usesService( databaseServiceProvider );
				}
		);
	}
}
