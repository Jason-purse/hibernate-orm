/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.model.naming;

import org.hibernate.boot.model.source.spi.AttributePath;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.loader.PropertyPath;

/**
 * An ImplicitNamingStrategy implementation which uses full composite paths
 * extracted from AttributePath, as opposed to just the terminal property part.
 *
 * 基于组合路径 - 根据属性路径抓取 - 对比仅仅 抓取的末尾属性 ...
 *
 * Mainly a port of the older DefaultComponentSafeNamingStrategy class implementing
 * the no longer supported NamingStrategy contract
 *
 * 主要是旧的DefaultComponentSafeNamingStrategy 实现的一个兼容版本  - 因为老的 DefaultComponentSafeNamingStrategy  不再支持命名 策略约定 ....
 *
 * @author Steve Ebersole
 * @author Emmanuel Bernard
 */
public class ImplicitNamingStrategyComponentPathImpl extends ImplicitNamingStrategyJpaCompliantImpl {
	public static final ImplicitNamingStrategyComponentPathImpl INSTANCE = new ImplicitNamingStrategyComponentPathImpl();

	@Override
	protected String transformAttributePath(AttributePath attributePath) {
		final StringBuilder sb = new StringBuilder();
		process( attributePath, sb );
		return sb.toString();
	}

	public static void process(AttributePath attributePath, StringBuilder sb) {
		String property = attributePath.getProperty();
		final AttributePath parent = attributePath.getParent();
		if ( parent != null && StringHelper.isNotEmpty( parent.getProperty() ) ) {
			process( parent, sb );
			sb.append( '_' );
		}
		else if ( PropertyPath.IDENTIFIER_MAPPER_PROPERTY.equals( property ) ) {
			// skip it, do not pass go
			sb.append( "id" );
			return;
		}
		property = property.replace( "<", "" );
		property = property.replace( ">", "" );

		sb.append( property );
	}
}
