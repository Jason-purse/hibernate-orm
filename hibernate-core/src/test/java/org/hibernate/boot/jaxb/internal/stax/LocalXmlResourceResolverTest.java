/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.boot.jaxb.internal.stax;

import static org.assertj.core.api.Assertions.assertThat;

import javax.xml.stream.XMLStreamException;

import org.hibernate.testing.boot.ClassLoaderServiceTestingImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.assertj.core.api.InstanceOfAssertFactories;

/**
 * Test the resolution of known XML schemas/DTDs to local resources.
 * 测试已知的XML schemas / DTDs 到本地资源的解析 ...
 * <p>
 *   当它遇见XML schemas LocalXmlResourceResolver 看起来似乎并不会实际执行...
 * Note that when it comes to XML schemas,
 * LocalXmlResourceResolver doesn't seem to be actually invoked;
 *  这有一定的好处,因为当配置解析器的时候  我们自己设置了XML schema;
 * which makes sense since we set the XML schema ourselves when configuring the parser.
 * 因此这个测试 可能仅仅与DTDs相关,但是我们保持关注对于XML schema 的测试 - 仅仅为了以防万一...
 * So this test is probably only relevant for DTDs, but we keep tests about XML schemas too just in case.
 */
public class LocalXmlResourceResolverTest {

	private final LocalXmlResourceResolver resolver;

	public LocalXmlResourceResolverTest() {
		this.resolver = new LocalXmlResourceResolver( ClassLoaderServiceTestingImpl.INSTANCE );
	}

	@ParameterizedTest
	// 这个属性注解的值 将会作为@ParameterizedTest的参数注入
	// xsd hibernate 只是为了以防万一
	@CsvSource({
			// JPA 1.0 and 2.0 share the same namespace URI
			// NOTE: Behavior differs from Hibernate ORM 5, which resolves to org/hibernate/jpa/orm_2_0.xsd
			"http://java.sun.com/xml/ns/persistence/orm,org/hibernate/jpa/orm_1_0.xsd",
			// JPA 2.1 and 2.2 share the same namespace URI
			"http://xmlns.jcp.org/xml/ns/persistence/orm,org/hibernate/jpa/orm_2_1.xsd",
			"https://jakarta.ee/xml/ns/persistence/orm,org/hibernate/jpa/orm_3_0.xsd",

			// NOTE: Hibernate ORM 5 doesn't resolve persistence.xml XSDs to local resources,
			//       but Hibernate ORM 6+ does.
			// JPA 1.0 and 2.0 share the same namespace URI
			"http://java.sun.com/xml/ns/persistence,org/hibernate/jpa/persistence_1_0.xsd",
			// JPA 2.1 and 2.2 share the same namespace URI
			"http://xmlns.jcp.org/xml/ns/persistence,org/hibernate/jpa/persistence_2_1.xsd",
			"https://jakarta.ee/xml/ns/persistence,org/hibernate/jpa/persistence_3_0.xsd",

			// 遗留的xsd
			"http://www.hibernate.org/xsd/orm/hbm,org/hibernate/xsd/mapping/legacy-mapping-4.0.xsd",
			// mapping ??
			"http://www.hibernate.org/xsd/hibernate-mapping,org/hibernate/hibernate-mapping-4.0.xsd",
			// cfg 配置 / 遗留的配置 4.0
			"http://www.hibernate.org/xsd/orm/cfg,org/hibernate/xsd/cfg/legacy-configuration-4.0.xsd",
	})
	void resolve_namespace_localResource(String namespace, String expectedLocalResource) throws XMLStreamException {
		assertThat( resolver.resolveEntity( null, null, null, namespace ) )
				// 断言是一个输入流实例
				.asInstanceOf( InstanceOfAssertFactories.INPUT_STREAM )
				// 和这个类加载器获取的InputStream 具有相同的内容
				.hasSameContentAs( getClass().getClassLoader().getResourceAsStream( expectedLocalResource ) );
	}
	// 解析本地dtd 资源..
	@ParameterizedTest
	@CsvSource({
			"http://www.hibernate.org/dtd/hibernate-mapping,org/hibernate/hibernate-mapping-3.0.dtd",
			"https://www.hibernate.org/dtd/hibernate-mapping,org/hibernate/hibernate-mapping-3.0.dtd",

			"http://hibernate.org/dtd/hibernate-mapping,org/hibernate/hibernate-mapping-3.0.dtd",
			"https://hibernate.org/dtd/hibernate-mapping,org/hibernate/hibernate-mapping-3.0.dtd",

			"http://hibernate.sourceforge.net/hibernate-mapping,org/hibernate/hibernate-mapping-3.0.dtd",
			"https://hibernate.sourceforge.net/hibernate-mapping,org/hibernate/hibernate-mapping-3.0.dtd",

			"http://www.hibernate.org/dtd/hibernate-configuration,org/hibernate/hibernate-configuration-3.0.dtd",
			"https://www.hibernate.org/dtd/hibernate-configuration,org/hibernate/hibernate-configuration-3.0.dtd",

			"http://hibernate.org/dtd/hibernate-configuration,org/hibernate/hibernate-configuration-3.0.dtd",
			"https://hibernate.org/dtd/hibernate-configuration,org/hibernate/hibernate-configuration-3.0.dtd",

			"http://hibernate.sourceforge.net/hibernate-configuration,org/hibernate/hibernate-configuration-3.0.dtd",
			"https://hibernate.sourceforge.net/hibernate-configuration,org/hibernate/hibernate-configuration-3.0.dtd"
	})
	void resolve_dtd_localResource(String id, String expectedLocalResource) throws XMLStreamException {
		// publicId
		assertThat( resolver.resolveEntity( id, null, null, null ) )
				.asInstanceOf( InstanceOfAssertFactories.INPUT_STREAM )
				.hasSameContentAs( getClass().getClassLoader().getResourceAsStream( expectedLocalResource ) );

		// systemId
		assertThat( resolver.resolveEntity( null, id, null, null ) )
				.asInstanceOf( InstanceOfAssertFactories.INPUT_STREAM )
				.hasSameContentAs( getClass().getClassLoader().getResourceAsStream( expectedLocalResource ) );
	}

}