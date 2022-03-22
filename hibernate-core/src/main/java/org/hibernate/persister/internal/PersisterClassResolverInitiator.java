/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.persister.internal;

import java.util.Map;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.persister.spi.PersisterClassResolver;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * @author Steve Ebersole
 */
public class PersisterClassResolverInitiator implements StandardServiceInitiator<PersisterClassResolver> {

	public static final PersisterClassResolverInitiator INSTANCE = new PersisterClassResolverInitiator();
	// 提供了一个自定义的持久化类解析器...
	public static final String IMPL_NAME = "hibernate.persister.resolver";

	@Override
	public Class<PersisterClassResolver> getServiceInitiated() {
		return PersisterClassResolver.class;
	}

	@Override
	public PersisterClassResolver initiateService(Map<String, Object> configurationValues, ServiceRegistryImplementor registry) {
		final Object customImpl = configurationValues.get( IMPL_NAME );
		if ( customImpl == null ) {
			// hibernate 标准的持久化类解析器
			return new StandardPersisterClassResolver();
		}

		if ( customImpl instanceof PersisterClassResolver ) {
			return (PersisterClassResolver) customImpl;
		}
		//   判断是否为class
		final Class<? extends PersisterClassResolver> customImplClass = Class.class.isInstance( customImpl )
				? (Class<? extends PersisterClassResolver>) customImpl
				// 然后尝试 从服务注册机中获取  可能是一个引用名称
				: locate( registry, customImpl.toString() );

		try {
			return customImplClass.newInstance();
		}
		catch (Exception e) {
			throw new ServiceException( "Could not initialize custom PersisterClassResolver impl [" + customImplClass.getName() + "]", e );
		}
	}

	private Class<? extends PersisterClassResolver> locate(ServiceRegistryImplementor registry, String className) {
		// 但本质上就是通过类加载器服务   加载此类 ...
		return registry.getService( ClassLoaderService.class ).classForName( className );
	}
}
