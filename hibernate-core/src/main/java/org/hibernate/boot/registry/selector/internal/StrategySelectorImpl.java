/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.registry.selector.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.hibernate.HibernateException;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.registry.classloading.spi.ClassLoadingException;
import org.hibernate.boot.registry.selector.spi.StrategyCreator;
import org.hibernate.boot.registry.selector.spi.StrategySelectionException;
import org.hibernate.boot.registry.selector.spi.StrategySelector;

import org.jboss.logging.Logger;

/**
 * Standard implementation of the StrategySelector contract.
 *
 * @author Steve Ebersole
 */
public class StrategySelectorImpl implements StrategySelector {

	private static final Logger log = Logger.getLogger( StrategySelectorImpl.class );

	private static final StrategyCreator STANDARD_STRATEGY_CREATOR = strategyClass -> {
		try {
			return strategyClass.newInstance();
		}
		catch (Exception e) {
			throw new StrategySelectionException(
					String.format( "Could not instantiate named strategy class [%s]", strategyClass.getName() ),
					e
			);
		}
	};

	//Map based approach: most suited for explicit registrations from integrators
	// 通过策略命名的命名策略实现器
	// 对于从integrators中显式提供的是比较 合适的...
	// 注册的服务都会放在这里...
	private final Map<Class,Map<String,Class>> namedStrategyImplementorByStrategyMap = new ConcurrentHashMap<>();

	//"Lazy" approach: more efficient as we aim to not initialize all implementation classes;
	//this is preferable for internal services such as Dialect, as we have a significant amount of them, making
	//it worthwhile to try be a bit more efficient about them.

	// 惰性初始化,更高效的方式 - 因为我们不在初始化所有的实例化类
	// 这是一个内部服务的首选方式 ,例如方言, 因为它们的大量  存在 使得这种方式 很有效...
	private final Map<Class, LazyServiceResolver> lazyStrategyImplementorByStrategyMap = new ConcurrentHashMap<>();

	private final ClassLoaderService classLoaderService;

	/**
	 * Constructs a StrategySelectorImpl using the given class loader service.
	 *
	 * @param classLoaderService The class loader service usable by this StrategySelectorImpl instance.
	 */
	public StrategySelectorImpl(ClassLoaderService classLoaderService) {
		this.classLoaderService = classLoaderService;
	}

	public <T> void registerStrategyLazily(Class<T> strategy, LazyServiceResolver<T> resolver) {
		LazyServiceResolver previous = lazyStrategyImplementorByStrategyMap.put( strategy, resolver );
		if ( previous != null ) {
			throw new HibernateException( "Detected a second LazyServiceResolver replacing an existing LazyServiceResolver implementation for strategy " + strategy.getName() );
		}
	}

	@Override
	public <T> void registerStrategyImplementor(Class<T> strategy, String name, Class<? extends T> implementation) {
		final Map<String,Class> namedStrategyImplementorMap = namedStrategyImplementorByStrategyMap.computeIfAbsent(
				strategy,
				aClass -> new ConcurrentHashMap<>()
		);
		// 不允许重复添加
		final Class old = namedStrategyImplementorMap.put( name, implementation );
		if ( old == null ) {
			if ( log.isTraceEnabled() ) {
				log.trace(
						String.format(
								"Registering named strategy selector [%s] : [%s] -> [%s]",
								strategy.getName(),
								name,
								implementation.getName()
						)
				);
			}
		}
		else {
			if ( log.isDebugEnabled() ) {
				log.debug(
						String.format(
								"Registering named strategy selector [%s] : [%s] -> [%s] (replacing [%s])",
								strategy.getName(),
								name,
								implementation.getName(),
								old.getName()
						)
				);
			}
		}
	}

	@Override
	public <T> void unRegisterStrategyImplementor(Class<T> strategy, Class<? extends T> implementation) {
		final Map<String,Class> namedStrategyImplementorMap = namedStrategyImplementorByStrategyMap.get( strategy );
		if ( namedStrategyImplementorMap == null ) {
			log.debug( "Named strategy map did not exist on call to un-register" );
			return;
		}

		final Iterator itr = namedStrategyImplementorMap.values().iterator();
		while ( itr.hasNext() ) {
			final Class registered = (Class) itr.next();
			if ( registered.equals( implementation ) ) {
				itr.remove();
			}
		}

		// try to clean up after ourselves...
		// 如果这个策略为空了,也就没必要存在了 ...
		if ( namedStrategyImplementorMap.isEmpty() ) {
			namedStrategyImplementorByStrategyMap.remove( strategy );
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> selectStrategyImplementor(Class<T> strategy, String name) {

		final Map<String,Class> namedStrategyImplementorMap = namedStrategyImplementorByStrategyMap.get( strategy );
		if ( namedStrategyImplementorMap != null ) {
			final Class registered = namedStrategyImplementorMap.get( name );
			if ( registered != null ) {
				return (Class<T>) registered;
			}
		}

		// 当服务不存在时 查看 懒服务解析器 中是否包含,如果包含  解析...
		LazyServiceResolver lazyServiceResolver = lazyStrategyImplementorByStrategyMap.get( strategy );
		if ( lazyServiceResolver != null ) {

			Class resolve = lazyServiceResolver.resolve( name );
			if ( resolve != null ) {
				return resolve;
			}
		}

		try {
			// 没办法了  只能尝试类加载器 加载它 ...
			return classLoaderService.classForName( name );
		}
		catch (ClassLoadingException e) {
			throw new StrategySelectionException(
					"Unable to resolve name [" + name + "] as strategy [" + strategy.getName() + "]",
					e
			);
		}
	}

	@Override
	public <T> T resolveStrategy(Class<T> strategy, Object strategyReference) {
		return resolveDefaultableStrategy( strategy, strategyReference, (T) null );
	}

	@Override
	public <T> T resolveDefaultableStrategy(Class<T> strategy, Object strategyReference, final T defaultValue) {
		return resolveDefaultableStrategy(
				strategy,
				strategyReference,
				(Callable<T>) () -> defaultValue
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T resolveDefaultableStrategy(
			Class<T> strategy,
			Object strategyReference,
			Callable<T> defaultResolver) {
		return (T) resolveStrategy( strategy, strategyReference, defaultResolver, STANDARD_STRATEGY_CREATOR );
	}

	@Override
	public <T> T resolveStrategy(
			Class<T> strategy,
			Object strategyReference,
			T defaultValue,
			StrategyCreator<T> creator) {
		return resolveStrategy(
				strategy,
				strategyReference,
				(Callable<T>) () -> defaultValue,
				creator
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection getRegisteredStrategyImplementors(Class strategy) {
		LazyServiceResolver lazyServiceResolver = lazyStrategyImplementorByStrategyMap.get( strategy );
		if ( lazyServiceResolver != null ) {
			throw new StrategySelectionException( "Can't use this method on for strategy types which are embedded in the core library" );
		}
		final Map<String, Class> registrations = namedStrategyImplementorByStrategyMap.get( strategy );
		if ( registrations == null ) {
			return Collections.emptySet();
		}
		return new HashSet( registrations.values() );
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T resolveStrategy(
			Class<T> strategy,
			Object strategyReference,
			Callable<T> defaultResolver,
			StrategyCreator<T> creator) {
		if ( strategyReference == null ) {
			try {
				return defaultResolver.call();
			}
			catch (Exception e) {
				throw new StrategySelectionException( "Default-resolver threw exception", e );
			}
		}

		// 判断是否本就是 这种实例
		if ( strategy.isInstance( strategyReference ) ) {
			// 直接cast
			return strategy.cast( strategyReference );
		}
		// 根据这个引用判断 是Class 还是  reference
		final Class<? extends T> implementationClass;
		if ( strategyReference instanceof Class ) {
			// 如果是
			implementationClass = (Class<T>) strategyReference;
		}
		else {
			// 否则根据FQN / reference 选择
			implementationClass = selectStrategyImplementor( strategy, strategyReference.toString() );
		}

		try {
			// 最后通过creator 创建
			return creator.create( implementationClass );
		}
		catch (Exception e) {
			throw new StrategySelectionException(
					String.format( "Could not instantiate named strategy class [%s]", implementationClass.getName() ),
					e
			);
		}
	}
}
