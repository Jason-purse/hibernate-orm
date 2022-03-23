/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.Internal;
import org.hibernate.boot.archive.spi.InputStreamAccess;
import org.hibernate.boot.internal.MetadataBuilderImpl;
import org.hibernate.boot.jaxb.internal.XmlSources;
import org.hibernate.boot.jaxb.spi.Binding;
import org.hibernate.boot.jaxb.spi.XmlSource;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.MetadataBuilderFactory;
import org.hibernate.boot.spi.XmlMappingBinderAccess;
import org.hibernate.internal.CoreLogging;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.SerializationException;

import org.w3c.dom.Document;

/**
 * Entry point for working with sources of O/R mapping metadata, either
 * in the form of annotated classes, or as XML mapping documents.
 *
 * 使用 OR 映射元数据源的入口点， 注解类的形式 或者xml 映射文档 ....
 * <p>
 * Note that XML mappings may be expressed using the JPA {@code orm.xml}
 * format, or in Hibernate's legacy {@code .hbm.xml} format.
 *
 * 注意到xml 映射也许使用JPA (orm.xml)格式进行表达 .... 或者 Hibernate 遗留的 xxx.hbm.xml 形式表达
 * <p>
 *     MetadataSources的实例也许通过实例化获取, 使用 MetadataSources无参构造器实例化
 *     // 客户端必须注册资源  并且 调用buildMetadata 或者 getMetadataBuilder 去定制化源如何被处理(通过注册的命名策略 或者其他)
 * An instance of {@code MetadataSources} may be obtained simply by
 * instantiation, using {@link #MetadataSources() new MetadataSources()}.
 * The client must register sources and then call {@link #buildMetadata()},
 * or use {@link #getMetadataBuilder()} to customize how the sources are
 * processed (by registering naming strategies, etc).
 * <p>
 *     另一个方式是直接使用MetadataSources 工作, 以及 Metadata  以及Configuration ...
 * As an alternative to working directly with {@code MetadataSources}, and
 * {@link Metadata}, a program may use {@link org.hibernate.cfg.Configuration}.
 *
 * @author Steve Ebersole
 *
 * @since 5.0
 */
public class MetadataSources implements Serializable {
	private static final CoreMessageLogger LOG = CoreLogging.messageLogger( MetadataSources.class );

	private final ServiceRegistry serviceRegistry;
	private final ClassLoaderService classLoaderService;
	private final boolean disableXmlMappingBinders;

	private XmlMappingBinderAccess xmlMappingBinderAccess;

	private List<Binding<?>> xmlBindings;
	private LinkedHashSet<Class<?>> annotatedClasses;
	private LinkedHashSet<String> annotatedClassNames;
	private LinkedHashSet<String> annotatedPackages;

	private Map<String,Class<?>> extraQueryImports;

	/**
	 * Create a new instance, using a default {@link BootstrapServiceRegistry}.
	 */
	public MetadataSources() {
		this( new BootstrapServiceRegistryBuilder().build() );
	}

	/**
	 * Create a new instance using the given {@link ServiceRegistry}.
	 *
	 * 通过给定给的服务注册机创建实例
	 *
	 * @param serviceRegistry The service registry to use.
	 */
	public MetadataSources(ServiceRegistry serviceRegistry) {
		// service registry really should be either BootstrapServiceRegistry or StandardServiceRegistry type...
		if ( ! isExpectedServiceRegistryType( serviceRegistry ) ) {
			if ( LOG.isDebugEnabled() ) {
				LOG.debugf(
						"Unexpected ServiceRegistry type [%s] encountered during building of MetadataSources; may cause " +
								"problems later attempting to construct MetadataBuilder",
						serviceRegistry.getClass().getName()
				);
			}
		}
		this.serviceRegistry = serviceRegistry;
		this.classLoaderService = serviceRegistry.getService( ClassLoaderService.class );
		// 禁用 xml 映射Binder ?
		this.disableXmlMappingBinders = false;
	}

	/**
	 * Consider this an SPI, used by Quarkus(一个java 相关的框架 - 类似于云原生的理念)
	 */
	public MetadataSources(ServiceRegistry serviceRegistry, boolean disableXmlMappingBinders) {
		Objects.requireNonNull( serviceRegistry );
		this.serviceRegistry = serviceRegistry;
		this.classLoaderService = serviceRegistry.getService( ClassLoaderService.class );
		this.disableXmlMappingBinders = disableXmlMappingBinders;
	}

	protected static boolean isExpectedServiceRegistryType(ServiceRegistry serviceRegistry) {
		return serviceRegistry instanceof BootstrapServiceRegistry
			|| serviceRegistry instanceof StandardServiceRegistry;
	}

	public XmlMappingBinderAccess getXmlMappingBinderAccess() {
		if ( disableXmlMappingBinders ) {
			return null;
		}
		if ( xmlMappingBinderAccess == null ) {
			xmlMappingBinderAccess = new XmlMappingBinderAccess( serviceRegistry );
		}
		return xmlMappingBinderAccess;
	}

	public List<Binding<?>> getXmlBindings() {
		return xmlBindings == null ? Collections.emptyList() : xmlBindings;
	}

	public Collection<String> getAnnotatedPackages() {
		return annotatedPackages == null ? Collections.emptySet() : annotatedPackages;
	}

	public Collection<Class<?>> getAnnotatedClasses() {
		return annotatedClasses == null ? Collections.emptySet() : annotatedClasses;
	}

	public Collection<String> getAnnotatedClassNames() {
		return annotatedClassNames == null ? Collections.emptySet() : annotatedClassNames;
	}

	public Map<String,Class<?>> getExtraQueryImports() {
		return extraQueryImports;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	/**
	 * Get a builder for metadata where non-default options can be specified.
	 *
	 * @return The built metadata.
	 */
	public MetadataBuilder getMetadataBuilder() {
		// 获取一个meta 的构建器
		return getCustomBuilderOrDefault( new MetadataBuilderImpl(this) );
	}

	/**
	 * Get a builder for metadata where non-default options can be specified.
	 *
	 * @return The built metadata.
	 */
	@Internal
	public MetadataBuilder getMetadataBuilder(StandardServiceRegistry serviceRegistry) {
		return getCustomBuilderOrDefault( new MetadataBuilderImpl(this, serviceRegistry ) );
	}

	/**
	 * In case a custom {@link MetadataBuilderFactory} creates a custom builder,
	 * return that one, otherwise return the default builder.
	 */
	private MetadataBuilder getCustomBuilderOrDefault(MetadataBuilderImpl defaultBuilder) {

		// spi 加载
		Collection<MetadataBuilderFactory> discoveredBuilderFactories =
				serviceRegistry.getService( ClassLoaderService.class )
						.loadJavaServices( MetadataBuilderFactory.class );

		MetadataBuilder builder = null;
		List<String> activeFactoryNames = null;

		for ( MetadataBuilderFactory discoveredBuilderFactory : discoveredBuilderFactories) {
			final MetadataBuilder returnedBuilder =
					discoveredBuilderFactory.getMetadataBuilder( this, defaultBuilder );
			if ( returnedBuilder != null ) {
				if ( activeFactoryNames == null ) {
					activeFactoryNames = new ArrayList<>();
				}
				activeFactoryNames.add( discoveredBuilderFactory.getClass().getName() );
				builder = returnedBuilder;
			}
		}

		if ( activeFactoryNames != null && activeFactoryNames.size() > 1 ) {
			throw new HibernateException(
					"Multiple active MetadataBuilder definitions were discovered : " +
							String.join(", ", activeFactoryNames)
			);
		}

		return builder != null ? builder : defaultBuilder;
	}

	/**
	 * Shorthand form of calling {@link #getMetadataBuilder()} and using its
	 * {@link MetadataBuilder#build()} method in cases where the application
	 * wants to accept the defaults.
	 *
	 * @return The built metadata.
	 */
	public Metadata buildMetadata() {
		return getMetadataBuilder().build();
	}

	public Metadata buildMetadata(StandardServiceRegistry serviceRegistry) {
		return getMetadataBuilder( serviceRegistry ).build();
	}

	/**
	 * Read metadata from the annotations attached to the given class.
	 *
	 * @param annotatedClass The class containing annotations
	 *
	 * @return this (for method chaining)
	 */
	public MetadataSources addAnnotatedClass(Class<?> annotatedClass) {
		if ( annotatedClasses == null ) {
			annotatedClasses = new LinkedHashSet<>();
		}
		annotatedClasses.add( annotatedClass );
		return this;
	}

	/**
	 * Vararg form of {@link #addAnnotatedClass(Class)}.
	 */
	public MetadataSources addAnnotatedClasses(Class<?>... annotatedClasses) {
		if ( annotatedClasses != null && annotatedClasses.length > 0 ) {
			if ( this.annotatedClasses == null ) {
				this.annotatedClasses = new LinkedHashSet<>();
			}
			Collections.addAll( this.annotatedClasses, annotatedClasses );
		}
		return this;
	}

	/**
	 * Read metadata from the annotations attached to the given class. The
	 * important distinction here is that the {@link Class} will not be
	 * accessed until later, which is important for on-the-fly bytecode
	 * enhancement
	 *
	 * @param annotatedClassName The name of a class containing annotations
	 *
	 * @return this (for method chaining)
	 */
	public MetadataSources addAnnotatedClassName(String annotatedClassName) {
		if ( annotatedClassNames == null ) {
			annotatedClassNames = new LinkedHashSet<>();
		}
		annotatedClassNames.add( annotatedClassName );
		return this;
	}

	/**
	 * Vararg form of {@link #addAnnotatedClassName(String)}.
	 */
	public MetadataSources addAnnotatedClassNames(String... annotatedClassNames) {
		if ( annotatedClassNames != null && annotatedClassNames.length > 0 ) {
			Collections.addAll( this.annotatedClassNames, annotatedClassNames );
		}
		return this;
	}

	public MetadataSources addQueryImport(String importedName, Class<?> target) {
		if ( extraQueryImports == null ) {
			extraQueryImports = new HashMap<>();
		}

		extraQueryImports.put( importedName, target );

		return this;
	}

	/**
	 * Read package-level metadata.
	 *
	 * @param packageName java package name without trailing '.', cannot be {@code null}
	 *
	 * @return this (for method chaining)
	 */
	public MetadataSources addPackage(String packageName) {
		if ( packageName == null ) {
			throw new IllegalArgumentException( "The specified package name cannot be null" );
		}

		if ( packageName.endsWith( "." ) ) {
			packageName = packageName.substring( 0, packageName.length() - 1 );
		}

		addPackageInternal( packageName );
		return this;
	}

	private void addPackageInternal(String packageName) {
		if ( annotatedPackages == null ) {
			annotatedPackages = new LinkedHashSet<>();
		}
		annotatedPackages.add( packageName );
	}

	/**
	 * Read package-level metadata.
	 *
	 * @param packageRef Java Package reference
	 *
	 * @return this (for method chaining)
	 */
	public MetadataSources addPackage(Package packageRef) {
		addPackageInternal( packageRef.getName() );
		return this;
	}

	/**
	 * Read mappings as an application resourceName (i.e. classpath lookup).
	 *
	 * @param name The resource name
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addResource(String name) {
		final XmlSource xmlSource = XmlSources.fromResource( name, classLoaderService );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * Read mappings from a particular XML file.
	 * <p>
	 * The given path is resolved using {@link File#File(String)}.
	 *
	 * @param path The path to a file
	 *
	 * @return this (for method chaining purposes)
	 *
	 * @see #addFile(File)
	 */
	public MetadataSources addFile(String path) {
		addFile( new File( path ) );
		return this;
	}

	/**
	 * Read mappings from a particular XML file.
	 *
	 * @param file The reference to the XML file
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addFile(File file) {
		final XmlSource xmlSource = XmlSources.fromFile( file );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * Add XML mapping bindings created from an arbitrary source by the
	 * {@linkplain #getXmlMappingBinderAccess() binder}.
	 *
	 * @param binding The binding
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addXmlBinding(Binding<?> binding) {
		getXmlBindingsForWrite().add( binding );
		return this;
	}

	/**
	 * Add a {@link #addCacheableFile(File) cached mapping file}.
	 * <p>
	 * The given path is resolved using {@link File#File(String)}.
	 *
	 * @param path The path to a file
	 *
	 * @return this (for method chaining purposes)
	 *
	 * @see #addCacheableFile(File)
	 */
	public MetadataSources addCacheableFile(String path) {
		addCacheableFile( new File( path ) );
		return this;
	}

	/**
	 * Add a {@link #addCacheableFile(File) cached mapping file}.
	 * <p>
	 * The given path is resolved using {@link File#File(String)}.
	 *
	 * @param path The path to a file
	 *
	 * @return this (for method chaining purposes)
	 *
	 * @see #addCacheableFile(File)
	 */
	public MetadataSources addCacheableFile(String path, File cacheDirectory) {
		addCacheableFile( new File( path ), cacheDirectory );
		return this;
	}

	/**
	 * Add a cached mapping file. A cached file is a serialized representation of
	 * the DOM structure of a particular mapping. It is saved from a previous call
	 * as a file with the name {@code {xmlFile}.bin} where {@code {xmlFile}} is the
	 * name of the original mapping file.
	 * </p>
	 * If a cached {@code {xmlFile}.bin} exists and is newer than {@code {xmlFile}},
	 * the {@code {xmlFile}.bin} file will be read directly. Otherwise {@code {xmlFile}}
	 * is read and then serialized to {@code {xmlFile}.bin} for use the next time.
	 *
	 * @param file The cacheable mapping file to be added, {@code {xmlFile}} in above discussion.
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addCacheableFile(File file) {
		return addCacheableFile( file, file.getParentFile() );
	}

	/**
	 * Add a cached mapping file.  A cached file is a serialized representation of
	 * the DOM structure of a particular mapping. It is saved from a previous call
	 * as a file with the name {@code {xmlFile}.bin} where {@code {xmlFile}} is the
	 * name of the original mapping file.
	 * </p>
	 * If a cached {@code {xmlFile}.bin} exists and is newer than {@code {xmlFile}},
	 * the {@code {xmlFile}.bin} file will be read directly. Otherwise {@code {xmlFile}}
	 * is read and then serialized to {@code {xmlFile}.bin} for use the next time.
	 *
	 * @param file The cacheable mapping file to be added, {@code {xmlFile}} in above discussion.
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addCacheableFile(File file, File cacheDirectory) {
		final XmlSource xmlSource = XmlSources.fromCacheableFile( file, cacheDirectory );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * <b>INTENDED FOR TESTSUITE USE ONLY!</b>
	 * <p/>
	 * Much like {@link #addCacheableFile(File)} except that here we will fail
	 * immediately if the cache version cannot be found or used for whatever reason.
	 *
	 * @param file The xml file, not the bin!
	 *
	 * @return The dom "deserialized" from the cached file.
	 *
	 * @throws SerializationException Indicates a problem deserializing the cached dom tree
	 * @throws MappingNotFoundException Indicates that the cached file was not found or was not usable.
	 */
	public MetadataSources addCacheableFileStrictly(File file) throws SerializationException {
		final XmlSource xmlSource = XmlSources.fromCacheableFile( file, true );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * <b>INTENDED FOR TESTSUITE USE ONLY!</b>
	 * <p/>
	 * Much like {@link #addCacheableFile(File)} except that here we will fail
	 * immediately if the cache version cannot be found or used for whatever reason.
	 *
	 * @param file The xml file, not the bin!
	 *
	 * @return The dom "deserialized" from the cached file.
	 *
	 * @throws SerializationException Indicates a problem deserializing the cached dom tree
	 * @throws MappingNotFoundException Indicates that the cached file was not found or was not usable.
	 */
	public MetadataSources addCacheableFileStrictly(File file, File cacheDir) throws SerializationException {
		final XmlSource xmlSource = XmlSources.fromCacheableFile( file, cacheDir, true );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * Read metadata from an {@link InputStream} access
	 *
	 * @param xmlInputStreamAccess Access to an input stream containing a DOM.
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addInputStream(InputStreamAccess xmlInputStreamAccess) {
		final XmlSource xmlSource = XmlSources.fromStream( xmlInputStreamAccess );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * Read metadata from an {@link InputStream}.
	 *
	 * @param xmlInputStream The input stream containing a DOM.
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addInputStream(InputStream xmlInputStream) {
		final XmlSource xmlSource = XmlSources.fromStream( xmlInputStream );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * Read mappings from a {@link URL}
	 *
	 * @param url The url for the mapping document to be read.
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addURL(URL url) {
		final XmlSource xmlSource = XmlSources.fromUrl( url );
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder()  ) );
		return this;
	}

	/**
	 * Read all {@code .hbm.xml} mappings from a jar file.
	 * <p/>
	 * Assumes that any file named {@code *.hbm.xml} is a mapping document.
	 * This method does not support {@code orm.xml} files!
	 *
	 * @param jar a jar file
	 *
	 * @return this (for method chaining purposes)
	 */
	public MetadataSources addJar(File jar) {
		final XmlMappingBinderAccess binderAccess = getXmlMappingBinderAccess();
		XmlSources.fromJar(
				jar,
				xmlSource -> getXmlBindingsForWrite().add( xmlSource.doBind( binderAccess.getMappingBinder() ) )
		);
		return this;
	}

	private List<Binding<?>> getXmlBindingsForWrite() {
		if ( xmlBindings == null ) {
			xmlBindings = new ArrayList<>();
		}
		return xmlBindings;
	}

	/**
	 * Read all {@code .hbm.xml} mapping documents from a directory tree.
	 * <p/>
	 * Assumes that any file named {@code *.hbm.xml} is a mapping document.
	 * This method does not support {@code orm.xml} files!
	 *
	 * @param dir The directory
	 *
	 * @return this (for method chaining purposes)
	 *
	 * @throws org.hibernate.MappingException Indicates problems reading the jar file or
	 * processing the contained mapping documents.
	 */
	public MetadataSources addDirectory(File dir) {
		File[] files = dir.listFiles();
		if ( files != null && files.length > 0 ) {
			for ( File file : files ) {
				if ( file.isDirectory() ) {
					addDirectory( file );
				}
				else if ( file.getName().endsWith( ".hbm.xml" ) ) {
					addFile( file );
				}
			}
		}
		return this;
	}

}
