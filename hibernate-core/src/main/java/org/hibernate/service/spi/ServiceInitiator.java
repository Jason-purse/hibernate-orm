/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.service.spi;

import org.hibernate.service.Service;

/**
 * Base contract for an initiator of a service.
 * 服务初始化器的基本约定 ...
 *
 * @author Steve Ebersole
 */
public interface ServiceInitiator<R extends Service> {
	/**
	 * Obtains the service role initiated by this initiator.  Should be unique within a registry
	 * 在注册机中应该是唯一的,通过此Initiator 获取一个初始化的服务角色 ...
	 *
	 * @return The service role.
	 */
	Class<R> getServiceInitiated();
}
