package cn.sexycode.myjpa.metamodel.model.convert.spi;

import cn.sexycode.sql.type.descriptor.java.JavaTypeDescriptor;

import javax.persistence.AttributeConverter;

/**
 * BasicValueConverter extension for AttributeConverter-specific support
 *
 * @author Steve Ebersole
 */
public interface JpaAttributeConverter<O, R> extends BasicValueConverter<O, R> {
    JavaTypeDescriptor<AttributeConverter<O, R>> getConverterJavaTypeDescriptor();

    ManagedBean<AttributeConverter<O, R>> getConverterBean();

    BasicJavaDescriptor<O> getDomainJavaTypeDescriptor();

    BasicJavaDescriptor<R> getRelationalJavaTypeDescriptor();
}
