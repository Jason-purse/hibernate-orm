/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.orm;

import java.io.File;
import java.util.Collections;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.process.CommandLineArgumentProvider;

/**
 * @author Steve Ebersole
 */
public class FileCommandLineArgumentProvider implements CommandLineArgumentProvider {
	private final String argumentName;

	@InputDirectory
	@PathSensitive(PathSensitivity.RELATIVE)
	RegularFileProperty path;

	public FileCommandLineArgumentProvider(String argumentName, Project project) {
		this.argumentName = argumentName;
		path = project.getObjects().fileProperty(); // 创建一个Property(RegularFileProperty)
	}

	public FileCommandLineArgumentProvider(String argumentName, RegularFile path, Project project) {
		this( argumentName, project );
		this.path.set( path ); // 设置File
	}

	@Override
	public Iterable<String> asArguments() {
		final File pathAsFile = path.get().getAsFile();
		return Collections.singleton(
				String.format(
						"-D%s=%s",
						argumentName,
						pathAsFile.getAbsolutePath()
				)
		);
	}
}
