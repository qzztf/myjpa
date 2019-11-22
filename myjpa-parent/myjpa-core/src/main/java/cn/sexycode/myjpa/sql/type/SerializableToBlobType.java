package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.mapping.MappingException;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DynamicParameterizedType;
import cn.sexycode.myjpa.sql.type.descriptor.java.SerializableTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BlobTypeDescriptor;
import cn.sexycode.util.core.object.ReflectHelper;

import java.io.Serializable;
import java.util.Properties;

/**
 */
public class SerializableToBlobType<T extends Serializable> extends AbstractSingleColumnStandardBasicType<T>
        implements DynamicParameterizedType {

    public static final String CLASS_NAME = "classname";

    private static final long serialVersionUID = 1L;

    public SerializableToBlobType() {
        super(BlobTypeDescriptor.DEFAULT, new SerializableTypeDescriptor(Serializable.class));
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setParameterValues(Properties parameters) {
        ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);
        if (reader != null) {
            setJavaTypeDescriptor(new SerializableTypeDescriptor<T>(reader.getReturnedClass()));
        } else {
            String className = parameters.getProperty(CLASS_NAME);
            if (className == null) {
                throw new MappingException("No class name defined for type: " + SerializableToBlobType.class.getName());
            }
            try {
                setJavaTypeDescriptor(new SerializableTypeDescriptor<T>(ReflectHelper.classForName(className)));
            } catch (ClassNotFoundException e) {
                throw new MappingException("Unable to load class from " + CLASS_NAME + " parameter", e);
            }
        }
    }
}
