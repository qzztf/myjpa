/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;

import cn.sexycode.mybatis.jpa.binding.MappingException;
import cn.sexycode.mybatis.jpa.binding.Metadata;
import cn.sexycode.sql.type.DynamicParameterizedType;
import cn.sexycode.sql.type.Mapping;
import cn.sexycode.sql.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Any value that maps to columns.
 *
 * @author Gavin King
 */
public class SimpleValue implements KeyValue {
    private static final Logger log = LoggerFactory.getLogger(SimpleValue.class);

    public static final String DEFAULT_ID_GEN_STRATEGY = "assigned";


    private final List<Selectable> columns = new ArrayList<Selectable>();
    private final List<Boolean> insertability = new ArrayList<Boolean>();
    private final List<Boolean> updatability = new ArrayList<Boolean>();
    private final Metadata metadata;

    private String typeName;
    private Properties typeParameters;
    private boolean isVersion;
    private boolean isNationalized;
    private boolean isLob;

    private Properties identifierGeneratorProperties;
    private String identifierGeneratorStrategy = DEFAULT_ID_GEN_STRATEGY;
    private String nullValue;
    private Table table;
    private String foreignKeyName;
    private String foreignKeyDefinition;
    private boolean alternateUniqueKey;
    private boolean cascadeDeleteEnabled;

    private Type type;

    public SimpleValue(Metadata metadata) {
        this.metadata = metadata;
    }

    public SimpleValue(Metadata metadata, Table table) {
        this(metadata);
        this.table = table;
    }

    @Override
    public boolean isCascadeDeleteEnabled() {
        return cascadeDeleteEnabled;
    }

    public void setCascadeDeleteEnabled(boolean cascadeDeleteEnabled) {
        this.cascadeDeleteEnabled = cascadeDeleteEnabled;
    }

    public void addColumn(Column column) {
        addColumn(column, true, true);
    }

    public void addColumn(Column column, boolean isInsertable, boolean isUpdatable) {
        int index = columns.indexOf(column);
        if (index == -1) {
            columns.add(column);
            insertability.add(isInsertable);
            updatability.add(isUpdatable);
        } else {
            if (insertability.get(index) != isInsertable) {
                throw new IllegalStateException("Same column is added more than once with different values for isInsertable");
            }
            if (updatability.get(index) != isUpdatable) {
                throw new IllegalStateException("Same column is added more than once with different values for isUpdatable");
            }
        }
        column.setValue(this);
        column.setTypeIndex(columns.size() - 1);
    }

    public void addFormula(Formula formula) {
        columns.add(formula);
        insertability.add(false);
        updatability.add(false);
    }

    @Override
    public boolean hasFormula() {
        Iterator iter = getColumnIterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof Formula) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getColumnSpan() {
        return columns.size();
    }

    @Override
    public Iterator<Selectable> getColumnIterator() {
        return columns.iterator();
    }

    public List getConstraintColumns() {
        return columns;
    }

    public String getTypeName() {
        return typeName;
    }


    public void makeVersion() {
        this.isVersion = true;
    }

    public boolean isVersion() {
        return isVersion;
    }

    public void makeNationalized() {
        this.isNationalized = true;
    }

    public boolean isNationalized() {
        return isNationalized;
    }

    public void makeLob() {
        this.isLob = true;
    }

    public boolean isLob() {
        return isLob;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public void createForeignKey() throws MappingException {
    }

    @Override
    public void createForeignKeyOfEntity(String entityName) {
        if (!hasFormula() && !"none".equals(getForeignKeyName())) {
            ForeignKey fk = table.createForeignKey(getForeignKeyName(), getConstraintColumns(), entityName, getForeignKeyDefinition());
            fk.setCascadeDeleteEnabled(cascadeDeleteEnabled);
        }
    }


    public boolean isUpdateable() {
        //needed to satisfy KeyValue
        return true;
    }


    public Properties getIdentifierGeneratorProperties() {
        return identifierGeneratorProperties;
    }

    public String getNullValue() {
        return nullValue;
    }

    public Table getTable() {
        return table;
    }

    /**
     * Returns the identifierGeneratorStrategy.
     *
     * @return String
     */
    public String getIdentifierGeneratorStrategy() {
        return identifierGeneratorStrategy;
    }


    /**
     * Sets the identifierGeneratorProperties.
     *
     * @param identifierGeneratorProperties The identifierGeneratorProperties to set
     */
    public void setIdentifierGeneratorProperties(Properties identifierGeneratorProperties) {
        this.identifierGeneratorProperties = identifierGeneratorProperties;
    }

    /**
     * Sets the identifierGeneratorStrategy.
     *
     * @param identifierGeneratorStrategy The identifierGeneratorStrategy to set
     */
    public void setIdentifierGeneratorStrategy(String identifierGeneratorStrategy) {
        this.identifierGeneratorStrategy = identifierGeneratorStrategy;
    }

    /**
     * Sets the nullValue.
     *
     * @param nullValue The nullValue to set
     */
    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    public String getForeignKeyDefinition() {
        return foreignKeyDefinition;
    }

    public void setForeignKeyDefinition(String foreignKeyDefinition) {
        this.foreignKeyDefinition = foreignKeyDefinition;
    }

    public boolean isAlternateUniqueKey() {
        return alternateUniqueKey;
    }

    public void setAlternateUniqueKey(boolean unique) {
        this.alternateUniqueKey = unique;
    }

    public boolean isNullable() {
        Iterator itr = getColumnIterator();
        while (itr.hasNext()) {
            final Object selectable = itr.next();
            if (selectable instanceof Formula) {
                // if there are *any* formulas, then the Value overall is
                // considered nullable
                return true;
            } else if (!((Column) selectable).isNullable()) {
                // if there is a single non-nullable column, the Value
                // overall is considered non-nullable.
                return false;
            }
        }
        // nullable by default
        return true;
    }

    public boolean isSimpleValue() {
        return true;
    }

    public boolean isValid(Mapping mapping) throws MappingException {
        return getColumnSpan() == getType().getColumnSpan(mapping);
    }

    public Type getType() throws MappingException {
        if (type != null) {
            return type;
        }

        if (typeName == null) {
            throw new MappingException("No type name");
        }

        if (typeParameters != null
                && Boolean.valueOf(typeParameters.getProperty(DynamicParameterizedType.IS_DYNAMIC))
                && typeParameters.get(DynamicParameterizedType.PARAMETER_TYPE) == null) {
        }

//		Type result = metadata.getTypeResolver().heuristicType( typeName, typeParameters );
        // if this is a byte[] version/timestamp, then we need to use RowVersionType
        // instead of BinaryType (HHH-10413)
//		if ( isVersion && BinaryType.class.isInstance( result ) ) {
//			log.debug( "version is BinaryType; changing to RowVersionType" );
//			result = RowVersionType.INSTANCE;
//		}
		/*if ( result == null ) {
			String msg = "Could not determine type for: " + typeName;
			if ( table != null ) {
				msg += ", at table: " + table.getName();
			}
			if ( columns != null && columns.size() > 0 ) {
				msg += ", for columns: " + columns;
			}
			throw new MappingException( msg );
		}

		return result;*/
        return null;
    }


    public boolean isTypeSpecified() {
        return typeName != null;
    }

    public void setTypeParameters(Properties parameterMap) {
        this.typeParameters = parameterMap;
    }

    public Properties getTypeParameters() {
        return typeParameters;
    }

    public void copyTypeFrom(SimpleValue sourceValue) {
//		setTypeName( sourceValue.getTypeName() );
        setTypeParameters(sourceValue.getTypeParameters());

        type = sourceValue.type;
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' + columns.toString() + ')';
    }

    public Object accept(ValueVisitor visitor) {
        return visitor.accept(this);
    }

    public boolean[] getColumnInsertability() {
        return extractBooleansFromList(insertability);
    }

    public boolean[] getColumnUpdateability() {
        return extractBooleansFromList(updatability);
    }

    private static boolean[] extractBooleansFromList(List<Boolean> list) {
        final boolean[] array = new boolean[list.size()];
        int i = 0;
        for (Boolean value : list) {
            array[i++] = value;
        }
        return array;
    }


    private static final class ParameterTypeImpl implements DynamicParameterizedType.ParameterType {

        private final Class returnedClass;
        private final Annotation[] annotationsMethod;
        private final String catalog;
        private final String schema;
        private final String table;
        private final boolean primaryKey;
        private final String[] columns;

        private ParameterTypeImpl(Class returnedClass, Annotation[] annotationsMethod, String catalog, String schema,
                                  String table, boolean primaryKey, String[] columns) {
            this.returnedClass = returnedClass;
            this.annotationsMethod = annotationsMethod;
            this.catalog = catalog;
            this.schema = schema;
            this.table = table;
            this.primaryKey = primaryKey;
            this.columns = columns;
        }

        @Override
        public Class getReturnedClass() {
            return returnedClass;
        }

        @Override
        public Annotation[] getAnnotationsMethod() {
            return annotationsMethod;
        }

        @Override
        public String getCatalog() {
            return catalog;
        }

        @Override
        public String getSchema() {
            return schema;
        }

        @Override
        public String getTable() {
            return table;
        }

        @Override
        public boolean isPrimaryKey() {
            return primaryKey;
        }

        @Override
        public String[] getColumns() {
            return columns;
        }
    }
}
