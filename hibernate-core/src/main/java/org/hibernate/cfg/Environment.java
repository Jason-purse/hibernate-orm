/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Version;
import org.hibernate.bytecode.spi.BytecodeProvider;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.ConfigHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;

import org.jboss.logging.Logger;


/**
 * Provides access to configuration properties passed in {@link Properties} objects.
 * 通过properties 访问配置 属性
 * <p>
 * Hibernate has two property scopes:
 * <ul>
 *     1. 工厂级别的属性 - 当一个SessionFactory 配置并实例化的时候可以指定
 *     每一个实例可能有不同的属性值
 * <li><em>Factory-level</em> properties are specified when a
 * {@link org.hibernate.SessionFactory} is configured and instantiated. Each instance
 * might have different property values.
 * // 系统级别的属性 共享在所有的工厂实例上 并且它们总是可以通过Environment 的properties 进行检测 。。。
 * <li><em>System-level</em> properties are shared by all factory instances and are
 * always determined by the {@code Environment} properties in {@link #getProperties()}.
 * </ul>
 *  仅仅只有系统级别的属性是 可以进行优化 通过生成的字节码优化访问
 * The only system-level properties are {@value #USE_REFLECTION_OPTIMIZER} and
 * {@value #BYTECODE_PROVIDER}.
 * <p>
 *     环境属性通过 System#getProperties 和 hibernate.properties中收集...
 *     系统属性覆盖指定hibernate.properties中指定的属性 ...
 * {@code Environment} properties are populated by calling {@link System#getProperties()}
 * and then from a resource named {@code /hibernate.properties}, if it exists. System
 * properties override properties specified in {@code hibernate.properties}.
 * <p>
 *     SessionFactory 从 System#getProperties中获取属性
 *     以及hibernate.properteis中
 *     以及传递给Configuration的Properties
 * The {@link org.hibernate.SessionFactory} obtains properties from:
 * <ul>
 * <li>{@link System#getProperties() system properties},
 * <li>properties defined in a resource named {@code /hibernate.properties}, and
 * <li>any instance of {@link Properties} passed to {@link Configuration#addProperties}.
 * </ul>
 * <table>
 * <tr><td><b>Property(  属性 / 方法   )</b></td><td><b>Interpretation(解释/说明)</b></td></tr>
 * <tr>
 *     DIALECT  它的子类即可
 *   <td>{@value #DIALECT}</td>
 *   <td>name of {@link org.hibernate.dialect.Dialect} subclass</td>
 * </tr>
 * <tr>
 *     // 链接提供器
 *   <td>{@value #CONNECTION_PROVIDER}</td>
 *   <td>name of a {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider}
 *   // ConnectionProvider的提供器即可 ...
 *   subclass (if not specified heuristics are used)</td>
 * </tr>
 * // USER / user
 * <tr><td>{@value #USER}</td><td>database username</td></tr>
 * // pass / password
 * <tr><td>{@value #PASS}</td><td>database password</td></tr>
 * <tr>
 *     // url
 *   <td>{@value #URL}</td>
 *   <td>JDBC URL (when using {@link java.sql.DriverManager})</td>
 * </tr>
 * <tr>
 *     // driver
 *   <td>{@value #DRIVER}</td>
 *   <td>classname of JDBC driver</td>
 * </tr>
 * <tr>
 *     // isolation
 *   <td>{@value #ISOLATION}</td>
 *   // 使用驱动管理器的时候才有用 ..
 *   <td>JDBC transaction isolation level (only when using
 *     {@link java.sql.DriverManager})
 *   </td>
 * </tr>
 * // 连接池  最大数量
 *   <td>{@value #POOL_SIZE}</td>
 *   <td>the maximum size of the connection pool (only when using
 *     {@link java.sql.DriverManager})
 *   </td>
 * </tr>
 * <tr>
 *     // 数据源/   jndi 名称
 *   <td>{@value #DATASOURCE}</td>
 *   <td>datasource JNDI name (when using {@link javax.sql.DataSource})</td>
 * </tr>
 * <tr>
 *     //  jndi_url
 *   <td>{@value #JNDI_URL}</td><td>JNDI {@link javax.naming.InitialContext} URL</td>
 * </tr>
 * <tr>
 *     // jndi_class
 *   <td>{@value #JNDI_CLASS}</td><td>JNDI {@link javax.naming.InitialContext} class name</td>
 * </tr>
 * <tr>
 *     //  最大  外连接 抓取的深度
 *   <td>{@value #MAX_FETCH_DEPTH}</td>
 *   <td>maximum depth of outer join fetching</td>
 * </tr>
 * <tr>
 *     // 语句批处理 尺寸
 *   <td>{@value #STATEMENT_BATCH_SIZE}</td>
 *   <td>enable use of JDBC2 batch API for drivers which support it</td>
 * </tr>
 * <tr>
 *     // 一个语句抓取的结果长度...
 *   <td>{@value #STATEMENT_FETCH_SIZE}</td>
 *   <td>set the JDBC fetch size</td>
 * </tr>
 * <tr>
 *     // 使用生成的Key
 *   <td>{@value #USE_GET_GENERATED_KEYS}</td>
 *   // 默认使用java.sql.PreparedStatement#getGeneratedKeys()
 *   <td>enable use of JDBC3 {@link java.sql.PreparedStatement#getGeneratedKeys()}
 *   // 在插入完成之后抓取natively 生成的key
 *   // 需要JDBC3+ 驱动以及 1.4+的JRE环境
 *   to retrieve natively generated keys after insert. Requires JDBC3+ driver and
 *   JRE1.4+</td>
 * </tr>
 * <tr>
 *     // hbm2ddl 是否启用自动ddl 输出
 *   <td>{@value #HBM2DDL_AUTO}</td>
 *   <td>enable auto DDL export</td>
 * </tr>
 * <tr>
 *     // 默认的schema
 *     // 对于不合法的表(相当于不合规的表) 指定一个默认的shema 用于查询(指定的一个库)
 *   <td>{@value #DEFAULT_SCHEMA}</td>
 *   <td>use given schema name for unqualified tables (always optional)</td>
 * </tr>
 * <tr>
 *     // 默认的catalog
 *   <td>{@value #DEFAULT_CATALOG}</td>
 *   // 不合规的表使用给定的catalog name ...
 *   <td>use given catalog name for unqualified tables (always optional)</td>
 * </tr>
 * <tr>
 *     // jta  jta平台实现名称(一般不用 websphere 特殊环境使用)
 *   <td>{@value #JTA_PLATFORM}</td>
 *   <td>name of {@link org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform}
 *   implementation</td>
 * </tr>
 * </table>
 *
 * @see org.hibernate.SessionFactory
 * @author Gavin King
 */
public final class Environment implements AvailableSettings {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, Environment.class.getName());

	private static final BytecodeProvider BYTECODE_PROVIDER_INSTANCE;
	private static final boolean ENABLE_REFLECTION_OPTIMIZER;

	private static final Properties GLOBAL_PROPERTIES;

	/**
	 * 静态代码块获取一些信息
	 */
	static {
		Version.logVersion();

		GLOBAL_PROPERTIES = new Properties();
		//Set USE_REFLECTION_OPTIMIZER to false to fix HHH-227
		GLOBAL_PROPERTIES.setProperty( USE_REFLECTION_OPTIMIZER, Boolean.FALSE.toString() );

		try {
			InputStream stream = ConfigHelper.getResourceAsStream( "/hibernate.properties" );
			try {
				GLOBAL_PROPERTIES.load(stream);
				// 抹掉 敏感信息
				LOG.propertiesLoaded( ConfigurationHelper.maskOut( GLOBAL_PROPERTIES, PASS ) );
			}
			catch (Exception e) {
				LOG.unableToLoadProperties();
			}
			finally {
				try{
					stream.close();
				}
				catch (IOException ioe){
					LOG.unableToCloseStreamError( ioe );
				}
			}
		}
		catch (HibernateException he) {
			LOG.propertiesNotFound();
		}

		try {
			// systemProperties ...
			Properties systemProperties = System.getProperties();
		    // Must be thread-safe in case an application changes System properties during Hibernate initialization.
		    // See HHH-8383.
			// 有可能在Hibernate的初始化的过程中改变,系统属性,所以需要加锁 ....
			//
			synchronized (systemProperties) {
				// 由HashTable的 api 说明来说,在任何迭代器创建之后的修改  对于迭代器遍历来说都是未知的 ....
				// 快速失败特性不完全可靠 ....
				GLOBAL_PROPERTIES.putAll(systemProperties);
			}
		}
		catch (SecurityException se) {
			LOG.unableToCopySystemProperties();
		}
		// 然后  是否优化 ...
		ENABLE_REFLECTION_OPTIMIZER = ConfigurationHelper.getBoolean(USE_REFLECTION_OPTIMIZER, GLOBAL_PROPERTIES);
		if ( ENABLE_REFLECTION_OPTIMIZER ) {
			LOG.usingReflectionOptimizer();
		}
		// 构建字节码 提供器
		BYTECODE_PROVIDER_INSTANCE = buildBytecodeProvider( GLOBAL_PROPERTIES );
	}

	/**
	 * Should we use reflection optimization?
	 *
	 * @return True if reflection optimization should be used; false otherwise.
	 *
	 * @see #USE_REFLECTION_OPTIMIZER
	 * @see #getBytecodeProvider()
	 * @see BytecodeProvider#getReflectionOptimizer
	 *
	 * @deprecated Deprecated to indicate that the method will be moved to
	 * {@link org.hibernate.boot.spi.SessionFactoryOptions} /
	 * {@link org.hibernate.boot.SessionFactoryBuilder} - probably in 6.0.
	 * See <a href="https://hibernate.atlassian.net/browse/HHH-12194">HHH-12194</a> and
	 * <a href="https://hibernate.atlassian.net/browse/HHH-12193">HHH-12193</a> for details
	 */
	@Deprecated
	public static boolean useReflectionOptimizer() {
		return ENABLE_REFLECTION_OPTIMIZER;
	}

	/**
	 * @deprecated Deprecated to indicate that the method will be moved to
	 * {@link org.hibernate.boot.spi.SessionFactoryOptions} /
	 * {@link org.hibernate.boot.SessionFactoryBuilder} - probably in 6.0.
	 * See <a href="https://hibernate.atlassian.net/browse/HHH-12194">HHH-12194</a> and
	 * <a href="https://hibernate.atlassian.net/browse/HHH-12193">HHH-12193</a> for details
	 */
	@Deprecated
	public static BytecodeProvider getBytecodeProvider() {
		return BYTECODE_PROVIDER_INSTANCE;
	}

	/**
	 * Disallow instantiation
	 */
	private Environment() {
		throw new UnsupportedOperationException();
	}

	/**
	 * The {@link System#getProperties() system properties}, extended with all
	 * additional properties specified in {@code hibernate.properties}.
	 */
	public static Properties getProperties() {
		Properties copy = new Properties();
		copy.putAll(GLOBAL_PROPERTIES);
		return copy;
	}

	public static final String BYTECODE_PROVIDER_NAME_BYTEBUDDY = "bytebuddy";
	public static final String BYTECODE_PROVIDER_NAME_NONE = "none";
	public static final String BYTECODE_PROVIDER_NAME_DEFAULT = BYTECODE_PROVIDER_NAME_BYTEBUDDY;

	public static BytecodeProvider buildBytecodeProvider(Properties properties) {
		String provider = ConfigurationHelper.getString( BYTECODE_PROVIDER, properties, BYTECODE_PROVIDER_NAME_DEFAULT );
		return buildBytecodeProvider( provider );
	}

	private static BytecodeProvider buildBytecodeProvider(String providerName) {
		if ( BYTECODE_PROVIDER_NAME_NONE.equals( providerName ) ) {
			return new org.hibernate.bytecode.internal.none.BytecodeProviderImpl();
		}
		if ( BYTECODE_PROVIDER_NAME_BYTEBUDDY.equals( providerName ) ) {
			return new org.hibernate.bytecode.internal.bytebuddy.BytecodeProviderImpl();
		}

		LOG.bytecodeProvider( providerName );

		// there is no need to support plugging in a custom BytecodeProvider via FQCN:
		// - the static helper methods on this class are deprecated
		// - it's possible to plug a custom BytecodeProvider directly into the ServiceRegistry
		//
		// This also allows integrators to inject a BytecodeProvider instance which has some
		// state; particularly useful to inject proxy definitions which have been prepared in
		// advance.
		// See also https://hibernate.atlassian.net/browse/HHH-13804 and how this was solved in
		// Quarkus.

		LOG.unknownBytecodeProvider( providerName, BYTECODE_PROVIDER_NAME_DEFAULT );
		return new org.hibernate.bytecode.internal.bytebuddy.BytecodeProviderImpl();
	}
}
