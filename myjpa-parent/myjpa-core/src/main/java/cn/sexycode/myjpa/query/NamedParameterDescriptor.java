package cn.sexycode.myjpa.query;

import cn.sexycode.sql.type.Type;

/**
 * Descriptor regarding a named parameter.
 *
 * @author Steve Ebersole
 */
public class NamedParameterDescriptor extends AbstractParameterDescriptor {
    private final String name;

    /**
     * Constructs a NamedParameterDescriptor
     *
     * @param name           The name of the parameter
     * @param expectedType   The expected type of the parameter, according to the translator
     * @param sourceLocation The locations of the named parameters (aye aye aye)
     */
    public NamedParameterDescriptor(String name, Type expectedType, int sourceLocation) {
        super(sourceLocation, expectedType);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NamedParameterDescriptor that = (NamedParameterDescriptor) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
