/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.orm.jakarta;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test;
import org.gradle.language.jvm.tasks.ProcessResources;

import static org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME;
import static org.gradle.api.tasks.SourceSet.TEST_SOURCE_SET_NAME;

/**
 * @author Steve Ebersole
 */
public class JakartaPlugin implements Plugin<Project> {
	public static final String JAKARTA = "jakarta";

	@Override
	public void apply(Project project) {
		// 为任务类注册一个快捷 方式(欺骗import)
		// register short-names for the task classes (fake "import")
		// 获取扩展容器中的扩展属性. 设置名称...
		// 然后根据扩展DSL 来收集用户输入
		project.getExtensions().getExtraProperties().set( JakartaDirectoryTransformation.class.getSimpleName(), JakartaDirectoryTransformation.class );
		project.getExtensions().getExtraProperties().set( JakartaJarTransformation.class.getSimpleName(), JakartaJarTransformation.class );

		// 创建一个配置 api...
		final Configuration api = project.getConfigurations().create(
				"api",
				(configuration) -> {
					// 设置能够消费 false (相当于无法被传递依赖)
					configuration.setCanBeConsumed( false );
					// 能否被解析
					configuration.setCanBeResolved( false );
				}
		);
		// 创建一个implementation
		final Configuration implementation = project.getConfigurations().create(
				"implementation",
				(configuration) -> {
					// 同样 ....
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( false );
					// 继承于 api
					configuration.extendsFrom( api );
				}
		);
		// ... 编译 only
		final Configuration compileOnly = project.getConfigurations().create(
				"compileOnly",
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( false );
				}
		);
		// runtimeOnly
		final Configuration runtimeOnly = project.getConfigurations().create(
				"runtimeOnly",
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( false );
				}
		);
		// compileClasspath
		project.getConfigurations().create(
				"compileClasspath",
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( true );
					configuration.extendsFrom( compileOnly, implementation );
				}
		);


		project.getConfigurations().create(
				"runtimeClasspath",
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( true );
					configuration.extendsFrom( runtimeOnly, implementation );
				}
		);

		final Configuration testImplementation = project.getConfigurations().create(
				"testImplementation",
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( false );
					configuration.extendsFrom( implementation );
				}
		);

		final Configuration testCompileOnly = project.getConfigurations().create(
				"testCompileOnly",
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( false );
					configuration.extendsFrom( compileOnly );
				}
		);

		final Configuration testRuntimeOnly = project.getConfigurations().create(
				"testRuntimeOnly" ,
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( false );
					configuration.extendsFrom( runtimeOnly );
				}
		);

		final Configuration testCompileClasspath = project.getConfigurations().create(
				"testCompileClasspath" ,
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( true );
					configuration.extendsFrom( testImplementation, testCompileOnly );
				}
		);

		final Configuration testRuntimeClasspath = project.getConfigurations().create(
				"testRuntimeClasspath" ,
				(configuration) -> {
					configuration.setCanBeConsumed( false );
					configuration.setCanBeResolved( true );
					configuration.extendsFrom( testImplementation, testRuntimeOnly );
				}
		);

		// determine the "source" project
		// 判断 source 项目
		final String path = project.getPath();
		// 项目必须以-jakarta 结尾
		assert path.endsWith( "-jakarta" ) : "Project path did not end with `-jakarta`";
		// 获取源项目路径 sourceProjectPath
		final String sourceProjectPath = path.substring( 0, path.length() - 8 );
		// 获取此项目(真实)
		final Project sourceProject = project.getRootProject().project( sourceProjectPath );


		// Get tasks from the source project we will need
		// 获取此真实项目的任务容器 ..
		final TaskContainer sourceProjectTasks = sourceProject.getTasks();
		// 获取资源集 容器 ..
		final SourceSetContainer sourceProjectSourceSets = extractSourceSets( sourceProject );
		// 获取主要的资源集
		final SourceSet sourceProjectMainSourceSet = sourceProjectSourceSets.getByName( MAIN_SOURCE_SET_NAME );
		// 获取Jar任务名称
		final Jar sourceProjectJarTask = (Jar) sourceProjectTasks.getByName( sourceProjectMainSourceSet.getJarTaskName() );
		// 这会直接实例化任务
		// 获取 sourcesJar 任务
		final Jar sourceProjectSourcesJarTask = (Jar) sourceProjectTasks.getByName( sourceProjectMainSourceSet.getSourcesJarTaskName() );
		// 获取JavadocJar 任务
		final Jar sourceProjectJavadocJarTask = (Jar) sourceProjectTasks.getByName( sourceProjectMainSourceSet.getJavadocJarTaskName() );

		// 获取测试集
		final SourceSet sourceProjectTestSourceSet = sourceProjectSourceSets.getByName( TEST_SOURCE_SET_NAME );
		// 获取javaCompile 任务
		final JavaCompile sourceProjectCompileTestClassesTask = (JavaCompile) sourceProjectTasks.getByName( sourceProjectTestSourceSet.getCompileJavaTaskName() );
		// 获取处理资源任务
		final ProcessResources sourceProjectProcessTestResourcesTask = (ProcessResources) sourceProjectTasks.getByName( sourceProjectTestSourceSet.getProcessResourcesTaskName() );


		// Create the "jakartafication" assemble tasks
		// 创建 assemble 相关联的额任务
		final TaskContainer tasks = project.getTasks();

		// 迫切的实例化此任务 ...
		final Task jakartafyTask = tasks.create(
				"jakartafy",
				(task) -> {
					task.setDescription( "Performs all of the Jakarta transformations" );
					task.setGroup( JAKARTA );
				}
		);
		// 拿取build 目录
		final DirectoryProperty buildDirectory = project.getLayout().getBuildDirectory();

		// 创建 ...
		tasks.<JakartaJarTransformation>create(
				"jakartafyJar",
				JakartaJarTransformation.class,
				(JakartaJarTransformation  transformation) -> {
					transformation.dependsOn( sourceProjectJarTask );
					transformation.setDescription( "Transforms the source project's main jar" );
					transformation.setGroup( JAKARTA );
					// 设置约定值
					// 默认就是归档文件
					transformation.getSourceJar().convention( sourceProjectJarTask.getArchiveFile() );
					// 设置约定值
					transformation.getTargetJar().convention( buildDirectory.file( relativeArchiveFileName( project, null ) ) );
					// 这种 迫切的方式构建
					// 在这里设置依赖关系没有问题,但是如果在任务避免的方式下配置,这是一种反形式配置,未来gradle 会报错...
					jakartafyTask.dependsOn( transformation );
				}
		);

		tasks.create(
				"jakartafySourcesJar",
				JakartaJarTransformation.class,
				(transformation) -> {
					transformation.dependsOn( sourceProjectSourcesJarTask );
					transformation.setDescription( "Transforms the source project's sources jar" );
					transformation.setGroup( JAKARTA );
					transformation.getSourceJar().convention( sourceProjectSourcesJarTask.getArchiveFile() );
					transformation.getTargetJar().convention( buildDirectory.file( relativeArchiveFileName( project, "sources" ) ) );
					jakartafyTask.dependsOn( transformation );
				}
		);

		tasks.create(
				"jakartafyJavadocJar",
				JakartaJarTransformation.class,
				(transformation) -> {
					transformation.dependsOn( sourceProjectJavadocJarTask );
					transformation.setDescription( "Transforms the source project's javadoc jar" );
					transformation.setGroup( JAKARTA );
					transformation.getSourceJar().convention( sourceProjectJavadocJarTask.getArchiveFile() );
					transformation.getTargetJar().convention( buildDirectory.file( relativeArchiveFileName( project, "javadoc" ) ) );
					jakartafyTask.dependsOn( transformation );
				}
		);

		final Provider<Directory> testCollectDir = project.getLayout().getBuildDirectory().dir( "jakarta/collect/tests" );
		final Provider<Directory> testTransformedDir = project.getLayout().getBuildDirectory().dir( "jakarta/transformed/tests" );

		final Copy collectTests = tasks.create(
				"collectTests",
				Copy.class,
				(task) -> {
					task.dependsOn( sourceProjectCompileTestClassesTask, sourceProjectProcessTestResourcesTask );
					task.setDescription( "Collects all needed test classes and resources into a single directory for transformation" );
					task.setGroup( JAKARTA );
					task.from( sourceProjectTestSourceSet.getOutput() );
					task.into( testCollectDir );
				}
		);


		final JakartaDirectoryTransformation jakartafyTests = tasks.create(
				"jakartafyTests",
				JakartaDirectoryTransformation.class,
				(task) -> {
					task.dependsOn( collectTests );
					task.setDescription( "Jakartafies the tests in preparation for execution" );
					task.setGroup( JAKARTA );
					task.getSourceDirectory().convention( testCollectDir );
					task.getTargetDirectory().convention( testTransformedDir );
				}
		);

		tasks.create(
				"test",
				Test.class,
				(task) -> {
					task.dependsOn( jakartafyTests );
					task.setDescription( "Performs the jakartafied tests against the jakartafied artifact" );
					task.setGroup( JAKARTA );

					final ConfigurableFileCollection transformedTests = project.files( testTransformedDir );
					task.setTestClassesDirs( transformedTests );
					task.setClasspath( task.getClasspath().plus( transformedTests ).plus( testRuntimeClasspath ) );

					project.getLayout().getBuildDirectory();
					task.getBinaryResultsDirectory().convention( project.getLayout().getBuildDirectory().dir( "test-results/test/binary" ) );
					task.reports( (reports) -> {
						reports.getHtml().getOutputLocation().convention( buildDirectory.dir( "reports/tests/test" ) );
						reports.getJunitXml().getOutputLocation().convention( buildDirectory.dir( "test-results/test" ) );
					});
				}
		);
	}
	// 相对路径的ArchiveFileName
	public static String relativeArchiveFileName(Project project, String classifier) {
		// 项目名称加版本
		final StringBuilder nameBuilder = new StringBuilder( "lib/" );
		nameBuilder.append( project.getName() );
		nameBuilder.append( "-" ).append( project.getVersion() );
		if ( classifier != null ) {
			nameBuilder.append( "-" ).append( classifier );
		}
		// jar
		return nameBuilder.append( ".jar" ).toString();
	}

	public static SourceSetContainer extractSourceSets(Project project) {
		// 项目获取约定 然后获取一个插件 ...
//		final JavaPluginConvention javaPluginConvention = project.getConvention().findPlugin( JavaPluginConvention.class );
		project.getLogger().quiet("extension properties {}",project.getExtensions().getExtraProperties().getProperties());
		JavaPluginExtension javaPluginConvention = project.getExtensions().<JavaPluginExtension>findByType(JavaPluginExtension.class);
		assert javaPluginConvention != null;
		//
		return javaPluginConvention.getSourceSets();
	}
}
