/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.type.descriptor.java;

import java.io.Serializable;

import org.hibernate.SharedSessionContract;

/**
 * Describes the mutability aspects of a Java type.
 * 描述了Java类型的可变性方面
 * The term mutability refers to the fact that generally speaking
 * the aspects described by this contract are defined by whether the Java type's internal state is mutable or not.
 * 这个术语  可变性涉及到 面临 通常说的方面 - 由此约定定义 Java类型的内部状态是可变还是不可变..
 *
 * @author Steve Ebersole
 */
public interface MutabilityPlan<T> extends Serializable {
	/**
	 * Can the internal state of instances of {@code T} be changed?
	 *
	 * @return True if the internal state can be changed; false otherwise.
	 */
	boolean isMutable();

	/**
	 * Return a deep copy of the value.
	 *
	 * @param value The value to deep copy
	 *
	 * @return The deep copy.
	 */
	T deepCopy(T value);

	/**
	 * Return a disassembled representation of the value.  This is used to push values onto the
	 * second level cache.  Compliment to {@link #assemble}
	 *
	 * @see #assemble
	 */
	Serializable disassemble(T value, SharedSessionContract session);

	/**
	 * Assemble a previously {@linkplain #disassemble disassembled} value.  This is used when pulling values from the
	 * second level cache.  Compliment to {@link #disassemble}
	 *
	 * @see #disassemble
	 */
	T assemble(Serializable cached, SharedSessionContract session);
}
