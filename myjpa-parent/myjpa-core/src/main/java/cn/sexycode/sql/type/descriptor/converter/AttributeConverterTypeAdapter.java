/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type.descriptor.converter;


import cn.sexycode.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.sql.type.descriptor.java.ImmutableMutabilityPlan;
import cn.sexycode.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.sql.type.descriptor.java.MutabilityPlan;
import cn.sexycode.sql.type.descriptor.sql.SqlTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;

/**
 * Adapts the Hibernate Type contract to incorporate JPA AttributeConverter calls.
 *
 * @author Steve Ebersole
 */
public class AttributeConverterTypeAdapter<T> extends AbstractSingleColumnStandardBasicType<T> {
    private static final Logger log = LoggerFactory.getLogger(AttributeConverterTypeAdapter.class);

    public static final String NAME_PREFIX = "converted::";

    private final String name;
    private final String description;

    private final Class modelType;
    private final Class jdbcType;
    private final AttributeConverter<? extends T, ?> attributeConverter;

    private final MutabilityPlan<T> mutabilityPlan;

    @SuppressWarnings("unchecked")
    public AttributeConverterTypeAdapter(
            String name,
            String description,
            AttributeConverter<? extends T, ?> attributeConverter,
            SqlTypeDescriptor sqlTypeDescriptorAdapter,
            Class modelType,
            Class jdbcType,
            JavaTypeDescriptor<T> entityAttributeJavaTypeDescriptor) {
        super(sqlTypeDescriptorAdapter, entityAttributeJavaTypeDescriptor);
        this.name = name;
        this.description = description;
        this.modelType = modelType;
        this.jdbcType = jdbcType;
        this.attributeConverter = attributeConverter;

        this.mutabilityPlan =
                entityAttributeJavaTypeDescriptor.getMutabilityPlan().isMutable() ?
                        new AttributeConverterMutabilityPlanImpl<T>(attributeConverter) :
                        ImmutableMutabilityPlan.INSTANCE;

        log.debug("Created AttributeConverterTypeAdapter -> " + name);
    }

    @Override
    public String getName() {
        return name;
    }

    public Class getModelType() {
        return modelType;
    }

    public Class getJdbcType() {
        return jdbcType;
    }

    public AttributeConverter<? extends T, ?> getAttributeConverter() {
        return attributeConverter;
    }

    @Override
    protected MutabilityPlan<T> getMutabilityPlan() {
        return mutabilityPlan;
    }

    @Override
    public String toString() {
        return description;
    }
}
