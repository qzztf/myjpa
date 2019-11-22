package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.*;
import cn.sexycode.myjpa.sql.type.BasicType;
import cn.sexycode.myjpa.sql.type.Type;
import cn.sexycode.myjpa.sql.type.TypeFactory;
import cn.sexycode.myjpa.sql.type.UserType;
import cn.sexycode.util.core.object.ReflectHelper;

import java.io.Serializable;
import java.util.Properties;

/**
 * Acts as the contract for getting types and as the mediator between {@link BasicTypeRegistry} and {@link cn.sexycode.myjpa.sql.type.TypeFactory}.
 *
 * @author qzz
 */
public class TypeResolver implements Serializable {
    private final BasicTypeRegistry basicTypeRegistry;
    private final cn.sexycode.myjpa.sql.type.TypeFactory typeFactory;

    public TypeResolver() {
        this(new BasicTypeRegistry(), new cn.sexycode.myjpa.sql.type.TypeFactory());
    }

    public TypeResolver(BasicTypeRegistry basicTypeRegistry, cn.sexycode.myjpa.sql.type.TypeFactory typeFactory) {
        this.basicTypeRegistry = basicTypeRegistry;
        this.typeFactory = typeFactory;
    }


    public void registerTypeOverride(cn.sexycode.myjpa.sql.type.BasicType type) {
        basicTypeRegistry.register(type);
    }

    public void registerTypeOverride(cn.sexycode.myjpa.sql.type.UserType type, String[] keys) {
        basicTypeRegistry.register(type, keys);
    }

    public void registerTypeOverride(CompositeUserType type, String[] keys) {
        basicTypeRegistry.register(type, keys);
    }

    public TypeFactory getTypeFactory() {
        return typeFactory;
    }

    /**
     * Locate a Hibernate {@linkplain cn.sexycode.myjpa.sql.type.BasicType basic type} given (one of) its registration names.
     *
     * @param name The registration name
     * @return The registered type
     */
    public BasicType basic(String name) {
        return basicTypeRegistry.getRegisteredType(name);
    }

    /**
     * See {@link #heuristicType(String, Properties)}
     *
     * @param typeName The name (see heuristic algorithm discussion on {@link #heuristicType(String, Properties)}).
     * @return The deduced type; may be null.
     * @throws TypeException Can be thrown from {@link #heuristicType(String, Properties)}
     */
    public cn.sexycode.myjpa.sql.type.Type heuristicType(String typeName) throws TypeException {
        return heuristicType(typeName, null);
    }

    /**
     * Uses heuristics to deduce the proper {@link cn.sexycode.myjpa.sql.type.Type} given a string naming the type or Java class.
     * <p/>
     * The search goes as follows:<ol>
     * <li>search for a basic type with 'typeName' as a registration key</li>
     * <li>
     * look for 'typeName' as a class name and<ol>
     * <li>if it names a {@link cn.sexycode.myjpa.sql.type.Type} implementor, return an instance</li>
     * <li>if it names a {@link CompositeUserType} or a {@link UserType}, return an instance of class wrapped intot the appropriate {@link cn.sexycode.myjpa.sql.type.Type} adapter</li>
     * <li>if it implements {@link org.hibernate.classic.Lifecycle}, return the corresponding entity type</li>
     * <li>if it implements {@link Serializable}, return the corresponding serializable type</li>
     * </ol>
     * </li>
     * </ol>
     *
     * @param typeName   The name (see heuristic algorithm above).
     * @param parameters Any parameters for the type.  Only applied if built!
     * @return The deduced type; may be null.
     * @throws TypeException Indicates a problem attempting to resolve 'typeName' as a {@link Class}
     */
    public cn.sexycode.myjpa.sql.type.Type heuristicType(String typeName, Properties parameters) throws TypeException {
        Type type = basic(typeName);
        if (type != null) {
            return type;
        }

        try {
            Class typeClass = ReflectHelper.classForName(typeName);
            if (typeClass != null) {
                return typeFactory.byClass(typeClass, parameters);
            }
        } catch (ClassNotFoundException ignore) {
        }

        return null;
    }
}
