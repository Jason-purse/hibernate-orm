/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.id;

import java.util.Properties;
import jakarta.persistence.GeneratedValue;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.ExportableProducer;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * The general contract between a class that generates unique
 * identifiers and the {@code Session}. It is not intended that
 * this interface ever be exposed to the application. It <b>is</b>
 * intended that users implement this interface to provide
 * custom identifier generation strategies.
 *
 * 通常在生成唯一标识符的类 和  Session 的一个约定
 * 它并没有打算暴露给应用 ..
 * 它是打算让用户实现 此接口去提供自定义的标识符生成策略 ...
 * <p>
 * Implementors should provide a public default constructor.
 * 实现者应该提供一个公共的默认构造器
 * <p>
 * Implementations that accept configuration parameters should
 * also implement {@code Configurable}.
 * 	实现接受配置参数  应该也需要实现Configurable
 * <p>
 * Implementors <em>must</em> be thread-safe
 * 实现 必须线程安全 ...
 *
 * @author Gavin King
 *
 * @see PersistentIdentifierGenerator
 */
public interface IdentifierGenerator extends Configurable, ExportableProducer {
	/**
	 * The configuration parameter holding the entity name
	 */
	String ENTITY_NAME = "entity_name";

	/**
	 * The configuration parameter holding the JPA entity name
	 */
	String JPA_ENTITY_NAME = "jpa_entity_name";

	/**
	 * Used as a key to pass the name used as {@link GeneratedValue#generator()} to  the
	 * {@link IdentifierGenerator} as it is configured.
	 */
	String GENERATOR_NAME = "GENERATOR_NAME";

	/**
	 * The contributor that contributed this generator
	 */
	String CONTRIBUTOR_NAME = "CONTRIBUTOR";

	/**
	 * Configure this instance, given the value of parameters
	 * specified by the user as {@code &lt;param&gt;} elements.
	 * <p>
	 * This method is called just once, following instantiation, and before {@link #registerExportables(Database)}.
	 *
	 * @param type The id property type descriptor
	 * @param params param values, keyed by parameter name
	 * @param serviceRegistry Access to service that may be needed.
	 * @throws MappingException If configuration fails.
	 */
	@Override
	default void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
	}

	/**
	 * Register database objects used by this identifier generator, e.g. sequences, tables, etc.
	 * <p>
	 * This method is called just once, after {@link #configure(Type, Properties, ServiceRegistry)}.
	 *
	 * @param database The database instance
	 */
	@Override
	default void registerExportables(Database database) {
	}

	/**
	 * Initializes this instance, in particular pre-generates SQL as necessary.
	 * 初始化此实例,特别是在必要时提前生成SQL。
	 * <p>
	 *     这个方法应该在 #registerExportables(Database) 之后 调用,第一次使用前..
	 * This method is called after {@link #registerExportables(Database)}, before first use.
	 *
	 * @param context A context to help generate SQL strings
	 */
	default void initialize(SqlStringGenerationContext context) {
	}

	/**
	 * Generate a new identifier.
	 *
	 * @param session The session from which the request originates
	 * @param object the entity or collection (idbag) for which the id is being generated
	 *
	 * @return a new identifier
	 *
	 * @throws HibernateException Indicates trouble generating the identifier
	 */
	Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException;

	/**
	 * Check if JDBC batch inserts are supported.
	 *
	 * @return JDBC batch inserts are supported.
	 */
	default boolean supportsJdbcBatchInserts() {
		return true;
	}
}
