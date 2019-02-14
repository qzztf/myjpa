/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.mybatis.jpa.util.CollectionHelper;
import cn.sexycode.mybatis.jpa.util.StringHelper;
import org.dom4j.Document;
import org.hibernate.AnnotationException;
import org.hibernate.annotations.common.reflection.ClassLoadingException;
import org.hibernate.annotations.common.reflection.MetadataProviderInjector;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.boot.internal.MetadataBuildingContextRootImpl;
import org.hibernate.boot.jaxb.spi.Binding;
import org.hibernate.boot.model.process.spi.ManagedResources;
import org.hibernate.boot.model.source.spi.MetadataSourceProcessor;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.JpaOrmXmlPersistenceUnitDefaultAware;
import org.hibernate.boot.spi.JpaOrmXmlPersistenceUnitDefaultAware.JpaOrmXmlPersistenceUnitDefaults;
import org.hibernate.cfg.AnnotationBinder;
import org.hibernate.cfg.AttributeConverterDefinition;
import org.hibernate.cfg.InheritanceState;
import org.hibernate.cfg.annotations.reflection.AttributeConverterDefinitionCollector;
import org.hibernate.cfg.annotations.reflection.JPAMetadataProvider;
import org.jboss.jandex.IndexView;
import org.jboss.logging.Logger;

import javax.persistence.*;
import java.util.*;

/**
 * @author Steve Ebersole
 */
public class AnnotationMetadataSourceProcessorImpl implements MetadataSourceProcessor {

    private final LinkedHashSet<String> annotatedPackages = new LinkedHashSet<String>();


    public AnnotationMetadataSourceProcessorImpl(
            ManagedResources managedResources) {

        for (Class annotatedClass : managedResources.getAnnotatedClassReferences()) {
            categorizeAnnotatedClass(annotatedClass, attributeConverterManager);
        }
    }

    private void categorizeAnnotatedClass(Class annotatedClass, AttributeConverterManager attributeConverterManager) {
        // categorize it, based on assumption it does not fall into multiple categories
        if (annotatedClass.isAnnotationPresent(Converter.class)) {
            //noinspection unchecked
            attributeConverterManager.addAttributeConverter(annotatedClass);
        } else if (annotatedClass.isAnnotationPresent(Entity.class)
                || annotatedClass.isAnnotationPresent(MappedSuperclass.class)) {
            xClasses.add(annotatedClass);
        } else if (annotatedClass.isAnnotationPresent(Embeddable.class)) {
            xClasses.add(annotatedClass);
        } else {
//			log.debugf( "Encountered a non-categorized annotated class [%s]; ignoring", annotatedClass.getName() );
        }
    }


//	private Document toDom4jDocument(MappingBinder.DelayedOrmXmlData delayedOrmXmlData) {
//		// todo : do we need to build a DocumentFactory instance for use here?
//		//		historically we did that to set TCCL since, iirc, dom4j uses TCCL
//		org.dom4j.io.STAXEventReader staxToDom4jReader = new STAXEventReader();
//		try {
//			return staxToDom4jReader.readDocument( delayedOrmXmlData.getStaxEventReader() );
//		}
//		catch (XMLStreamException e) {
//			throw new MappingException(
//					"An error occurred transforming orm.xml document from StAX to dom4j representation ",
//					e,
//					delayedOrmXmlData.getOrigin()
//			);
//		}
//	}

    @Override
    public void prepare() {
        // use any persistence-unit-defaults defined in orm.xml
        ((JpaOrmXmlPersistenceUnitDefaultAware) rootMetadataBuildingContext.getBuildingOptions()).apply(
                new JpaOrmXmlPersistenceUnitDefaults() {
                    final Map persistenceUnitDefaults = reflectionManager.getDefaults();

                    @Override
                    public String getDefaultSchemaName() {
                        return StringHelper.nullIfEmpty((String) persistenceUnitDefaults.get("schema"));
                    }

                    @Override
                    public String getDefaultCatalogName() {
                        return StringHelper.nullIfEmpty((String) persistenceUnitDefaults.get("catalog"));
                    }

                    @Override
                    public boolean shouldImplicitlyQuoteIdentifiers() {
                        final Object isDelimited = persistenceUnitDefaults.get("delimited-identifier");
                        return isDelimited != null && isDelimited == Boolean.TRUE;
                    }
                }
        );

        rootMetadataBuildingContext.getMetadataCollector().getDatabase().adjustDefaultNamespace(
                rootMetadataBuildingContext.getBuildingOptions().getMappingDefaults().getImplicitCatalogName(),
                rootMetadataBuildingContext.getBuildingOptions().getMappingDefaults().getImplicitSchemaName()
        );

        AnnotationBinder.bindDefaults(rootMetadataBuildingContext);
        for (String annotatedPackage : annotatedPackages) {
            AnnotationBinder.bindPackage(annotatedPackage, rootMetadataBuildingContext);
        }
    }

    @Override
    public void processTypeDefinitions() {

    }

    @Override
    public void processQueryRenames() {

    }

    @Override
    public void processNamedQueries() {

    }

    @Override
    public void processAuxiliaryDatabaseObjectDefinitions() {

    }

    @Override
    public void processIdentifierGenerators() {

    }

    @Override
    public void processFilterDefinitions() {

    }

    @Override
    public void processFetchProfiles() {

    }

    @Override
    public void prepareForEntityHierarchyProcessing() {

    }

    @Override
    public void processEntityHierarchies(Set<String> processedEntityNames) {
        final List<XClass> orderedClasses = orderAndFillHierarchy(xClasses);
        Map<XClass, InheritanceState> inheritanceStatePerClass = AnnotationBinder.buildInheritanceStates(
                orderedClasses,
                rootMetadataBuildingContext
        );


        for (XClass clazz : orderedClasses) {
            if (processedEntityNames.contains(clazz.getName())) {
                log.debugf("Skipping annotated class processing of entity [%s], as it has already been processed", clazz);
                continue;
            }

            AnnotationBinder.bindClass(clazz, inheritanceStatePerClass, rootMetadataBuildingContext);
            processedEntityNames.add(clazz.getName());
        }
    }

    private List<XClass> orderAndFillHierarchy(List<XClass> original) {
        List<XClass> copy = new ArrayList<XClass>(original);
        insertMappedSuperclasses(original, copy);

        // order the hierarchy
        List<XClass> workingCopy = new ArrayList<XClass>(copy);
        List<XClass> newList = new ArrayList<XClass>(copy.size());
        while (workingCopy.size() > 0) {
            XClass clazz = workingCopy.get(0);
            orderHierarchy(workingCopy, newList, copy, clazz);
        }
        return newList;
    }

    private void insertMappedSuperclasses(List<XClass> original, List<XClass> copy) {
        for (XClass clazz : original) {
            XClass superClass = clazz.getSuperclass();
            while (superClass != null
                    && !reflectionManager.equals(superClass, Object.class)
                    && !copy.contains(superClass)) {
                if (superClass.isAnnotationPresent(Entity.class)
                        || superClass.isAnnotationPresent(MappedSuperclass.class)) {
                    copy.add(superClass);
                }
                superClass = superClass.getSuperclass();
            }
        }
    }

    private void orderHierarchy(List<XClass> copy, List<XClass> newList, List<XClass> original, XClass clazz) {
        if (clazz == null || reflectionManager.equals(clazz, Object.class)) {
            return;
        }
        //process superclass first
        orderHierarchy(copy, newList, original, clazz.getSuperclass());
        if (original.contains(clazz)) {
            if (!newList.contains(clazz)) {
                newList.add(clazz);
            }
            copy.remove(clazz);
        }
    }

    @Override
    public void postProcessEntityHierarchies() {

    }

    @Override
    public void processResultSetMappings() {

    }

    @Override
    public void finishUp() {

    }

    private static class AttributeConverterManager implements AttributeConverterDefinitionCollector {
        private final MetadataBuildingContextRootImpl rootMetadataBuildingContext;

        public AttributeConverterManager(MetadataBuildingContextRootImpl rootMetadataBuildingContext) {
            this.rootMetadataBuildingContext = rootMetadataBuildingContext;
        }

        @Override
        public void addAttributeConverter(AttributeConverterDefinition definition) {
            rootMetadataBuildingContext.getMetadataCollector().addAttributeConverter(definition);
        }

        public void addAttributeConverter(Class<? extends AttributeConverter> converterClass) {
            rootMetadataBuildingContext.getMetadataCollector().addAttributeConverter(converterClass);
        }
    }
}
