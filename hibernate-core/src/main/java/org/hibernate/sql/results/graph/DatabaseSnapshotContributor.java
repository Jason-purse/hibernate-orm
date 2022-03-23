/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.sql.results.graph;

import org.hibernate.query.spi.NavigablePath;
import org.hibernate.sql.ast.tree.from.TableGroup;

/**
 * Contract for model-parts which contribute to their container's
 * state array for database snapshots
 *
 * 为数据库快照   - 将部分模型合并到 容器状态数组中 ..
 *
 * @author Steve Ebersole
 */
public interface DatabaseSnapshotContributor extends Fetchable {

	/**
	 * Create a DomainResult to be used when selecting snapshots from the database.
	 * 从数据库中获取快照  用来创建DomainResult
	 * <p/>
	 * By default, simply use {@link #createDomainResult}
	 */
	default <T> DomainResult<T> createSnapshotDomainResult(
			NavigablePath navigablePath,
			TableGroup tableGroup,
			String resultVariable,
			DomainResultCreationState creationState) {
		return createDomainResult( navigablePath, tableGroup, null, creationState );
	}
}
