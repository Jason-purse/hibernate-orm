/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service.spi;
import java.util.Map;

/**
 * Allows the service to request access to the configuration properties for configuring itself.
 *
 * 允许服务请求访问 configuration properties 来配置 自己
 *
 * @author Steve Ebersole
 */
public interface Configurable {
	/**
	 * Configure the service.
	 *
	 * @param configurationValues The configuration properties.
	 */
	void configure(Map<String, Object> configurationValues);
}
