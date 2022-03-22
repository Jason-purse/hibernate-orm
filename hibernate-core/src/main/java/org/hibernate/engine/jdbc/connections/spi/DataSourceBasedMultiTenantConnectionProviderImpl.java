/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.connections.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jndi.spi.JndiService;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Stoppable;

/**
 * 一个多租户连接提供器 的具体实现  - 约定 基于大量已知的假设
 * A concrete implementation of the {@link MultiTenantConnectionProvider} contract bases on a number of
 * reasonable assumptions.
 * // 我们假设:
 * // 1. 所有必要的DataSource 来自命名的JNDI的 .. 通过租户标识符关联到单个基本的JNDI 上下文 ..
 * We assume that:<ul>
 *     <li>
 *         The {@link DataSource} instances are all available from JNDI named by the tenant identifier relative
 *         to a single base JNDI context
 *     </li>
 *
 *     // 2. Datasource 设置(设置了数据源)
 *     <li>
 *         {@value AvailableSettings#DATASOURCE} is a string naming either the {@literal any}
 *         data source or the base JNDI context.  If the latter, {@link #TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY} must
 *         also be set.
 *
 *         可以是数据源  或者JNDI 上下文 ,如果是后者     TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY 必须 设置 ..
 *     </li>
 * </ul>
 *
 * @author Steve Ebersole
 */
public class DataSourceBasedMultiTenantConnectionProviderImpl
		extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
		implements ServiceRegistryAwareService, Stoppable {

	/**
	 * Identifies the DataSource name to use for {@link #selectAnyDataSource} handling
	 *
	 * 表示数据源名称被用作selectAnyDataSource方法处理
	 */
	public static final String TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY = "hibernate.multi_tenant.datasource.identifier_for_any";

	// 多住户数据源 ..
	private Map<String,DataSource> dataSourceMap;
	private JndiService jndiService;
	private String tenantIdentifierForAny;
	private String baseJndiNamespace;

	@Override
	protected DataSource selectAnyDataSource() {
		return selectDataSource( tenantIdentifierForAny );
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		// 根据数据库源Key 获取 ...
		DataSource dataSource = dataSourceMap().get( tenantIdentifier );
		if ( dataSource == null ) {
			// 否则尝试 jndiService 获取
			dataSource = (DataSource) jndiService.locate( baseJndiNamespace + '/' + tenantIdentifier );
			dataSourceMap().put( tenantIdentifier, dataSource );
		}
		return dataSource;
	}

	private Map<String,DataSource> dataSourceMap() {
		if ( dataSourceMap == null ) {
			dataSourceMap = new ConcurrentHashMap<>();
		}
		return dataSourceMap;
	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		// 配置数据服务获取
		final Object dataSourceConfigValue = serviceRegistry.getService( ConfigurationService.class )
				// 获取配置
				.getSettings()
				.get( AvailableSettings.DATASOURCE );

		// 注册服务的 同时 就分离出数据库 ...
		// 必须是String
		if ( !(dataSourceConfigValue instanceof String) ) {
			throw new HibernateException( "Improper set up of DataSourceBasedMultiTenantConnectionProviderImpl" );
		}
		// jndiService
		final String jndiName = (String) dataSourceConfigValue;

		// 获取JNDI服务
		jndiService = serviceRegistry.getService( JndiService.class );

		// 不能获取 报错
		if ( jndiService == null ) {
			throw new HibernateException( "Could not locate JndiService from DataSourceBasedMultiTenantConnectionProviderImpl" );
		}

		//  命名空间无法解析
		final Object namedObject = jndiService.locate( jndiName );
		if ( namedObject == null ) {
			throw new HibernateException( "JNDI name [" + jndiName + "] could not be resolved" );
		}
		// 如果是DataSource
		if ( namedObject instanceof DataSource ) {
			// 拿取 / 后面的数据库引用名称
			final int loc = jndiName.lastIndexOf( '/' );
			// 拿取基本的jndi命名空间
			this.baseJndiNamespace = jndiName.substring( 0, loc );
			// 多租户标识器 也就是数据库引用名称
			this.tenantIdentifierForAny = jndiName.substring( loc + 1 );
			// 存入
			dataSourceMap().put( tenantIdentifierForAny, (DataSource) namedObject );
		}
		// 如果为上下文(单个上下文)  这里对比了非上下文和上下文的不同处理方式
		else if ( namedObject instanceof Context ) {

			this.baseJndiNamespace = jndiName;
			// 同样 多租户形式  例如存在jndi的形式下  需要 设置 TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY (住户id 作为key)
			this.tenantIdentifierForAny = (String) serviceRegistry.getService( ConfigurationService.class )
					.getSettings()
					.get( TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY );
			if ( tenantIdentifierForAny == null ) {
				throw new HibernateException( "JNDI name named a Context, but tenant identifier to use for ANY was not specified" );
			}
		}
		else {
			throw new HibernateException(
					"Unknown object type [" + namedObject.getClass().getName() +
							"] found in JNDI location [" + jndiName + "]"
			);
		}
	}

	@Override
	public void stop() {
		if ( dataSourceMap != null ) {
			dataSourceMap.clear();
			dataSourceMap = null;
		}
	}
}
