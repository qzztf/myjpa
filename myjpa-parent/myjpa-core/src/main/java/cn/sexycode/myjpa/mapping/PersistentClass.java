package cn.sexycode.myjpa.mapping;

import cn.sexycode.myjpa.binding.MappingException;
import cn.sexycode.myjpa.binding.MetadataBuildingContext;
import cn.sexycode.myjpa.metamodel.internal.MappedSuperclass;
import cn.sexycode.sql.mapping.Table;
import cn.sexycode.util.core.collection.JoinedIterator;
import cn.sexycode.util.core.exception.ClassLoadingException;
import cn.sexycode.util.core.service.ServiceRegistry;

import javax.persistence.criteria.Join;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Mapping for an entity.
 *
 */
public abstract class PersistentClass implements AttributeContainer, Serializable {

    public static final String NULL_DISCRIMINATOR_MAPPING = "null";
    public static final String NOT_NULL_DISCRIMINATOR_MAPPING = "not null";

    private final MetadataBuildingContext metadataBuildingContext;
    private String entityName;

    private String className;
    private transient Class mappedClass;

    private String proxyInterfaceName;
    private transient Class proxyInterface;

    private String jpaEntityName;

    private String discriminatorValue;
    private boolean lazy;
    private ArrayList properties = new ArrayList();
    private ArrayList declaredProperties = new ArrayList();
    private final ArrayList subclassProperties = new ArrayList();
    private final ArrayList subclassTables = new ArrayList();
    private boolean dynamicInsert;
    private boolean dynamicUpdate;
    private int batchSize = -1;
    private boolean selectBeforeUpdate;
    private java.util.Map metaAttributes;
    private ArrayList<Join> joins = new ArrayList<Join>();
    private final ArrayList subclassJoins = new ArrayList();
    private final java.util.List filters = new ArrayList();
    protected final Set synchronizedTables = new HashSet();
    private String loaderName;
    private Boolean isAbstract;
    private boolean hasSubselectLoadableCollections;

    // Custom SQL
    private String customSQLInsert;
    private boolean customInsertCallable;
    private String customSQLUpdate;
    private boolean customUpdateCallable;
    private String customSQLDelete;
    private boolean customDeleteCallable;

    private java.util.Map tuplizerImpls;

    private MappedSuperclass superMappedSuperclass;

    public PersistentClass(MetadataBuildingContext metadataBuildingContext) {
        this.metadataBuildingContext = metadataBuildingContext;
    }

    public ServiceRegistry getServiceRegistry() {
        return metadataBuildingContext.getBuildingOptions().getServiceRegistry();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.intern();
        this.mappedClass = null;
    }

    public String getProxyInterfaceName() {
        return proxyInterfaceName;
    }

    public void setProxyInterfaceName(String proxyInterfaceName) {
        this.proxyInterfaceName = proxyInterfaceName;
        this.proxyInterface = null;
    }

    public Class getMappedClass() {
        if (className == null) {
            return null;
        }

        try {
            if (mappedClass == null) {
                mappedClass = metadataBuildingContext.getBootstrapContext().getClassLoaderAccess()
                        .classForName(className);
            }
            return mappedClass;
        } catch (ClassLoadingException e) {
            throw new MappingException("entity class not found: " + className, e);
        }

    }
    // The following methods are added to support @MappedSuperclass in the metamodel
    public Iterator getDeclaredPropertyIterator() {
        ArrayList iterators = new ArrayList();
        iterators.add( declaredProperties.iterator() );
        return new JoinedIterator( iterators );
    }


    @Override
    public void addProperty(Property p) {
        properties.add( p );
        declaredProperties.add( p );
        p.setPersistentClass( this );
    }

    public Class getProxyInterface() {
        return null;
    }

    public boolean useDynamicInsert() {
        return dynamicInsert;
    }

    abstract int nextSubclassId();

    public abstract int getSubclassId();

    public boolean useDynamicUpdate() {
        return dynamicUpdate;
    }

    public void setDynamicInsert(boolean dynamicInsert) {
        this.dynamicInsert = dynamicInsert;
    }

    public void setDynamicUpdate(boolean dynamicUpdate) {
        this.dynamicUpdate = dynamicUpdate;
    }


    public String getDiscriminatorValue() {
        return discriminatorValue;
    }


    public abstract Table getTable();

    public String getEntityName() {
        return entityName;
    }

    public abstract boolean isMutable();

    public abstract boolean hasIdentifierProperty();


    public abstract boolean isInherited();

    public abstract boolean isPolymorphic();

    public abstract boolean isVersioned();

    public abstract String getNaturalIdCacheRegionName();

    public abstract String getCacheConcurrencyStrategy();

    public abstract PersistentClass getSuperclass();

    public abstract boolean isExplicitPolymorphism();

    public abstract boolean isDiscriminatorInsertable();

    //    public abstract Iterator getPropertyClosureIterator();

    //    public abstract Iterator getTableClosureIterator();

    //    public abstract Iterator getKeyClosureIterator();

    //    protected void addSubclassJoin(Join join) {
    //        subclassJoins.add(join);
    //    }

    //    protected void addSubclassTable(Table subclassTable) {
    //        subclassTables.add(subclassTable);
    //    }

    public void setEntityName(String entityName) {
        this.entityName = entityName == null ? null : entityName.intern();
    }

    public void setJpaEntityName(String jpaEntityName) {
        this.jpaEntityName = jpaEntityName;
    }

    public String getJpaEntityName() {
        return jpaEntityName;
    }
}
