/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.internal;

import org.hibernate.NotYetImplementedFor6Exception;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.metamodel.mapping.EmbeddableValuedModelPart;
import org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl;
import org.hibernate.metamodel.model.domain.spi.JpaMetamodelImplementor;
import org.hibernate.metamodel.spi.MappingMetamodelImplementor;
import org.hibernate.metamodel.spi.RuntimeMetamodelsImplementor;

/**
 * @author Steve Ebersole
 */
public class RuntimeMetamodelsImpl implements RuntimeMetamodelsImplementor {
	private JpaMetamodelImplementor jpaMetamodel;
	private MappingMetamodelImplementor mappingMetamodel;

	public RuntimeMetamodelsImpl() {
	}

	@Override
	public JpaMetamodelImplementor getJpaMetamodel() {
		return jpaMetamodel;
	}

	@Override
	public MappingMetamodelImplementor getMappingMetamodel() {
		return mappingMetamodel;
	}

	@Override
	public EmbeddableValuedModelPart getEmbedded(String role) {
		throw new NotYetImplementedFor6Exception( getClass() );
	}

	/** 鸡和蛋的问题, 因为有些事情尝试去使用 SessionFactory 在它准备好之前(特别是MappingMetamodel)
	 * Chicken-and-egg because things try to use the SessionFactory (specifically the MappingMetamodel)
	 * before it is ready.  So we do this fugly code...  因此我们写出了这样丑陋的代码
	 */
	public void finishInitialization(
			MetadataImplementor bootMetamodel,
			BootstrapContext bootstrapContext,
			SessionFactoryImpl sessionFactory) {
		final MappingMetamodelImpl mappingMetamodel = bootstrapContext.getTypeConfiguration().scope( sessionFactory );
		this.mappingMetamodel = mappingMetamodel;
		mappingMetamodel.finishInitialization( // 尝试完成mappingMetamodel的初始化 ..
				bootMetamodel,
				bootstrapContext,
				sessionFactory
		);

		this.jpaMetamodel = mappingMetamodel.getJpaMetamodel();
	}
}
