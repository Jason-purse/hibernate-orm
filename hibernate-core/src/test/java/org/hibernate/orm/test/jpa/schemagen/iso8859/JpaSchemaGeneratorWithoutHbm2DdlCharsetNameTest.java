/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.jpa.schemagen.iso8859;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.orm.test.jpa.schemagen.JpaSchemaGeneratorTest;

import org.hibernate.testing.RequiresDialect;

/**
 * @author Vlad Mihalcea
 */
@RequiresDialect( H2Dialect.class )
public class JpaSchemaGeneratorWithoutHbm2DdlCharsetNameTest
		extends JpaSchemaGeneratorTest {

	@Override
	public String getScriptFolderPath() {
		return super.getScriptFolderPath() + "iso8859/";
	}

	protected String encodedName() {
		return "sch" + String.valueOf( '\uFFFD' ) +"magen-test";
	}
}