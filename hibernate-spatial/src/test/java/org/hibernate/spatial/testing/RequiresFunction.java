/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.spatial.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.testing.orm.junit.SessionFactoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Indicates that the annotated test class/method should
 * only be run when the current Dialect supports the function.
 *
 * @author Karel Maesen
 */

@Inherited
@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)

@ExtendWith({ SessionFactoryExtension.class, RequiresFunctionExtension.class })
public @interface RequiresFunction {
	/**
	 * The key for the function (as used in the SqmFunctionRegistry)
	 */
	String key();

}
