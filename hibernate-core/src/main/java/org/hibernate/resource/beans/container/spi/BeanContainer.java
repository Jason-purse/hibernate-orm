/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.resource.beans.container.spi;

import org.hibernate.resource.beans.spi.BeanInstanceProducer;
import org.hibernate.service.spi.Stoppable;

/**
 * Represents a backend "bean container" - CDI, Spring, etc
 *
 * IOC 性质的容器 ...
 *
 * // 通过这个属性 和Spring IOC  容器集成
 * @see org.hibernate.cfg.AvailableSettings#BEAN_CONTAINER
 *
 * @author Steve Ebersole
 */
public interface BeanContainer extends Stoppable {
	interface LifecycleOptions {
		// 能够使用  缓存的引用 ??
		boolean canUseCachedReferences();
		// 使用jpa 编译创建 ??
		boolean useJpaCompliantCreation();
	}

	<B> ContainedBean<B> getBean(
			Class<B> beanType,
			LifecycleOptions lifecycleOptions,
			BeanInstanceProducer fallbackProducer);

	<B> ContainedBean<B> getBean(
			String name,
			Class<B> beanType,
			LifecycleOptions lifecycleOptions,
			BeanInstanceProducer fallbackProducer);
}
