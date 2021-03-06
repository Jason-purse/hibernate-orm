/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.engine.jdbc.batch.spi;

import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.service.Service;

/**
 * A builder for {@link Batch} instances.
 *
 * Batch 实例 的 一个构建者
 * <p>
 *
 *     能够选择一个自定义的BatchBuilder 使用(通过 特定的配置属性)
 * A custom {@code BatchBuilder} may be selected using the configuration property
 * {@value org.hibernate.cfg.AvailableSettings#BATCH_STRATEGY}.
 *
 * @author Steve Ebersole
 */
public interface BatchBuilder extends Service {
	/**
	 * Build a batch.
	 *
	 * @param key Value to uniquely identify a batch
	 * @param jdbcCoordinator The JDBC coordinator with which to coordinate efforts
	 *
	 * @return The built batch
	 */
	Batch buildBatch(BatchKey key, JdbcCoordinator jdbcCoordinator);
}
