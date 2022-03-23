/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.model.convert.spi;

import org.hibernate.Incubating;
import org.hibernate.type.descriptor.java.JavaType;

/**
 * Support for basic-value conversions.
 * 支持基础类型转换的 基础数据转换
 *
 * Conversions might be defined by:
 *		 约定由 自定义的JPA 的AttributeConverter转换 / 隐含的,基于Java类型 ..(枚举)
 * 		* a custom JPA {@link jakarta.persistence.AttributeConverter},
 * 		* implicitly, based on the Java type (e.g., enums)
 * 	    * etc
 *
 * @param <D> The Java type we can use to represent the domain (object) type
 * @param <R> The Java type we can use to represent the relational type
 *
 * @author Steve Ebersole
 */
@Incubating
public interface BasicValueConverter<D,R> {
	/**
	 * Convert the relational form just retrieved from JDBC ResultSet into
	 * the domain form.
	 */
	D toDomainValue(R relationalForm);

	/**
	 * Convert the domain form into the relational form in preparation for
	 * storage into JDBC
	 * 转换domain 形式到关系形式 为存储到JDBC做准备
	 */
	R toRelationalValue(D domainForm);

	/**
	 * Descriptor for the Java type for the domain portion of this converter
	 */
	JavaType<D> getDomainJavaType();

	/**
	 * Descriptor for the Java type for the relational portion of this converter
	 */
	JavaType<R> getRelationalJavaType();
}
