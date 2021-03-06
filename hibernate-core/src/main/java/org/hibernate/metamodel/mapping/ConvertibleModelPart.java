/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.metamodel.mapping;

import org.hibernate.metamodel.model.convert.spi.BasicValueConverter;

/**
 * A BasicValuedModelPart which can have a converter associated with it
 * 它包含一个与它联系的基础值转换器
 *
 * @author Steve Ebersole
 */
public interface ConvertibleModelPart extends BasicValuedModelPart {
	/**
	 * Get the value converter applied to this model part if any
	 */
	BasicValueConverter getValueConverter();
}
