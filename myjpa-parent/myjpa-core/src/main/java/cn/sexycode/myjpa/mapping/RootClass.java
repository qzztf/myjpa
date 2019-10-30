package cn.sexycode.myjpa.mapping;

import javax.persistence.Table;
import java.util.Iterator;

/**
 * The root class of an inheritance hierarchy
 *
 */
public class RootClass extends PersistentClass {

    public static final String DEFAULT_IDENTIFIER_COLUMN_NAME = "id";
    public static final String DEFAULT_DISCRIMINATOR_COLUMN_NAME = "class";

    private boolean polymorphic;
    private String cacheConcurrencyStrategy;
    private String cacheRegionName;
    private String naturalIdCacheRegionName;
    private boolean lazyPropertiesCacheable = true;
    private boolean mutable = true;
    private boolean embeddedIdentifier;
    private boolean explicitPolymorphism;
    private Class entityPersisterClass;
    private boolean forceDiscriminator;
    private String where;
    private Table table;
    private boolean discriminatorInsertable = true;
    private int nextSubclassId;
    private boolean cachingExplicitlyRequested;

    @Override
    int nextSubclassId() {
        return ++nextSubclassId;
    }

    @Override
    public int getSubclassId() {
        return 0;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public Table getTable() {
        return table;
    }


    @Override
    public boolean isInherited() {
        return false;
    }

    @Override
    public boolean isPolymorphic() {
        return polymorphic;
    }

    @Override
    public boolean isVersioned() {
        return false;
    }

    @Override
    public String getNaturalIdCacheRegionName() {
        return null;
    }

    @Override
    public String getCacheConcurrencyStrategy() {
        return null;
    }

    public void setPolymorphic(boolean polymorphic) {
        this.polymorphic = polymorphic;
    }

    public RootClass getRootClass() {
        return this;
    }

    @Override
    public boolean isExplicitPolymorphism() {
        return explicitPolymorphism;
    }

    @Override
    public boolean isDiscriminatorInsertable() {
        return false;
    }

    @Override
    public Iterator getPropertyClosureIterator() {
        return null;
    }

    @Override
    public Iterator getTableClosureIterator() {
        return null;
    }

    @Override
    public Iterator getKeyClosureIterator() {
        return null;
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public boolean hasIdentifierProperty() {
        return false;
    }


    @Override
    public PersistentClass getSuperclass() {
        return null;
    }


    public boolean isCachingExplicitlyRequested() {
        return cachingExplicitlyRequested;
    }
}
