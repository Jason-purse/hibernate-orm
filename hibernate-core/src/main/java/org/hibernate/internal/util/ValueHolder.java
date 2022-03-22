/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.internal.util;

/**
 * Represents a "final" value that is initialized either {@link #ValueHolder(Object) up front} or once at some point
 * {@linkplain #ValueHolder(DeferredInitializer) after} declaration.
 *
 * 呈现一个final 的数据  要么 通过ValueHolder (Ojbect)  初始化 / 或者在某一个点 初始化 ...ValueHolder(DeferredInitializer)
 * 
 * Note: If a Serializable class has a {@link ValueHolder} property, that property should be declared transient!
 *
 * 如果一个序列化类有 ValueHolder 属性,那么 这个属性应该 声明为 transient;
 *
 * @author Steve Ebersole
 */
public class ValueHolder<T> {

	/**
	 * The snippet that generates the initialization value.
	 *
	 * @param <T>
	 */
	public interface DeferredInitializer<T> {
		/**
		 * Build the initialization value.
		 * <p/>
		 * Implementation note: returning {@code null} is "ok" but will cause this method to keep being called.
		 *
		 * @return The initialization value.
		 */
		T initialize();
	}

	private final DeferredInitializer<T> valueInitializer;
	private T value;

	/**
	 * Instantiates a {@link ValueHolder} with the specified initializer.
	 *
	 * @param valueInitializer The initializer to use in {@link #getValue} when value not yet known.
	 */
	public ValueHolder(DeferredInitializer<T> valueInitializer) {
		this.valueInitializer = valueInitializer;
	}

	@SuppressWarnings( {"unchecked"})
	public ValueHolder(T value) {
		this( NO_DEFERRED_INITIALIZER );
		this.value = value;
	}

	public T getValue() {
		if ( value == null ) {
			value = valueInitializer.initialize();
		}
		return value;
	}

	private static final DeferredInitializer NO_DEFERRED_INITIALIZER = new DeferredInitializer() {
		@Override
		public Void initialize() {
			return null;
		}
	};
}
