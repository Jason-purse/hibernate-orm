/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.cfgxml.spi;

import org.hibernate.service.Service;

/**
 * Allows access to any {@code cfg.xml} files specified for bootstrapping.
 *
 * 通过bootstrap 访问任何cfg.xml 文件
 *
 * @author Steve Ebersole
 */
public interface CfgXmlAccessService extends Service {
	String LOADED_CONFIG_KEY = "hibernate.boot.CfgXmlAccessService.key";

	LoadedConfig getAggregatedConfig();
}
