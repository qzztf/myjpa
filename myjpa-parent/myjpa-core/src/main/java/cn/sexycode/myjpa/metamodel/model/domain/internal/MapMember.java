package cn.sexycode.myjpa.metamodel.model.domain.internal;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Acts as a virtual Member definition for dynamic (Map-based) models.
 *
 * @author Brad Koehn
 */
public class MapMember implements Member {
    private String name;

    private final Class<?> type;

    public MapMember(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public int getModifiers() {
        return Modifier.PUBLIC;
    }

    @Override
    public boolean isSynthetic() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return null;
    }
}
