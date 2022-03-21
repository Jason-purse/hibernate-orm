/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.boot.xsd;

import org.hibernate.Internal;

/**
 * Support for XSD handling related to Hibernate's `cfg.xml` and
 * JPA's `persistence.xml`.
 *
 * 对XSD处理 的支持 - Hibernate的 cfg.xml 以及 JPA的 persistence.xml处理
 *
 *
 * The implementation attempts to not load XsdDescriptor instances which are not
 * necessary and favours memory efficiency over CPU efficiency, as this is expected
 * to be used only during bootstrap.
 * 这个实现不会 尝试加载XsdDescriptor 实例 - 这在CPU效率上没有必要并优化内存效率 ..
 * 因为这个仅仅在引导阶段才使用 ;
 *
 * @author Steve Ebersole
 * @author Sanne Grinovero
 */
@Internal
@SuppressWarnings("unused")
public class ConfigXsdSupport {

	/**
	 * 任何访问都需要同步
	 * Needs synchronization on any access.
	 * // 支持 以下的Key
	 * Custom keys:
	 * 0: cfgXml // cfgXml (cfg 解析)
	 * 1: JPA 1.0 // JPA...
	 * 2: JPA 2.0
	 * 3: JPA 2.1
	 * 4: JPA 2.2 (default)
	 * 5: Jakarta Persistence 3.0
	 */
	private static final XsdDescriptor[] xsdCache = new XsdDescriptor[6];

	public XsdDescriptor latestJpaDescriptor() {
		return getJPA22();
	}

	public static boolean shouldBeMappedToLatestJpaDescriptor(String uri) {
		// JPA 1.0 and 2.0 share the same namespace URI
		return getJPA10().getNamespaceUri().matches( uri );
	}
	 //决定一个版本用来 解析XSD
	public XsdDescriptor jpaXsd(String version) {
		switch ( version ) {
			case "1.0": {
				return getJPA10();
			}
			case "2.0": {
				return getJPA20();
			}
			case "2.1": {
				return getJPA21();
			}
			case "2.2": {
				return getJPA22();
			}
			case "3.0": {
				return getJPA30();
			}
			default: {
				throw new IllegalArgumentException( "Unrecognized JPA persistence.xml XSD version : `" + version + "`" );
			}
		}
	}

	public static XsdDescriptor cfgXsd() {
		final int index = 0;
		synchronized ( xsdCache ) {
			XsdDescriptor cfgXml = xsdCache[index];
			if ( cfgXml == null ) {
				cfgXml = LocalXsdResolver.buildXsdDescriptor(
						"org/hibernate/xsd/cfg/legacy-configuration-4.0.xsd",
						"4.0" ,
						"http://www.hibernate.org/xsd/orm/cfg"
				);
				xsdCache[index] = cfgXml;
			}
			return cfgXml;
		}
	}

	public static XsdDescriptor getJPA10() {
		final int index = 1;
		synchronized ( xsdCache ) {
			XsdDescriptor jpa10 = xsdCache[index];
			if ( jpa10 == null ) {
				// 本地 XSD解析器 构建
				jpa10 = LocalXsdResolver.buildXsdDescriptor(
						"org/hibernate/jpa/persistence_1_0.xsd",
						"1.0",
						"http://java.sun.com/xml/ns/persistence"
				);
				xsdCache[index] = jpa10;
			}
			return jpa10;
		}
	}

	public static XsdDescriptor getJPA20() {
		final int index = 2;
		synchronized ( xsdCache ) {
			XsdDescriptor jpa20 = xsdCache[index];
			if ( jpa20 == null ) {
				jpa20 = LocalXsdResolver.buildXsdDescriptor(
						"org/hibernate/jpa/persistence_2_0.xsd",
						"2.0" ,
						"http://java.sun.com/xml/ns/persistence"
				);
				xsdCache[index] = jpa20;
			}
			return jpa20;
		}
	}

	public static XsdDescriptor getJPA21() {
		final int index = 3;
		synchronized ( xsdCache ) {
			XsdDescriptor jpa21 = xsdCache[index];
			if ( jpa21 == null ) {
				jpa21 = LocalXsdResolver.buildXsdDescriptor(
						"org/hibernate/jpa/persistence_2_1.xsd",
						"2.1",
						"http://xmlns.jcp.org/xml/ns/persistence"
				);
				xsdCache[index] = jpa21;
			}
			return jpa21;
		}
	}

	public static XsdDescriptor getJPA22() {
		final int index = 4;
		synchronized ( xsdCache ) {
			XsdDescriptor jpa22 = xsdCache[index];
			if ( jpa22 == null ) {
				jpa22 = LocalXsdResolver.buildXsdDescriptor(
						"org/hibernate/jpa/persistence_2_2.xsd",
						"2.2",
						"http://xmlns.jcp.org/xml/ns/persistence"
				);
				xsdCache[index] = jpa22;
			}
			return jpa22;
		}
	}

	public static XsdDescriptor getJPA30() {
		final int index = 5;
		synchronized ( xsdCache ) {
			XsdDescriptor jpa30 = xsdCache[index];
			if ( jpa30 == null ) {
				jpa30 = LocalXsdResolver.buildXsdDescriptor(
						"org/hibernate/jpa/persistence_3_0.xsd",
						"3.0",
						"https://jakarta.ee/xml/ns/persistence"
				);
				xsdCache[index] = jpa30;
			}
			return jpa30;
		}
	}
	
}
