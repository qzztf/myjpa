package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.util.core.cls.ReflectionManager;
import cn.sexycode.util.core.cls.XClass;
import cn.sexycode.util.core.cls.internal.JavaReflectionManager;
import cn.sexycode.util.core.collection.CollectionHelper;
import cn.sexycode.util.core.exception.AnnotationException;
import cn.sexycode.util.core.exception.ClassLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Converter;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.util.*;

public class AnnotationMetadataSourceProcessorImpl extends SimpleMetadataSourceProcessorImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationMetadataSourceProcessorImpl.class);

    private final MetadataBuildingContextRootImpl rootMetadataBuildingContext;

    private final LinkedHashSet<String> annotatedPackages = new LinkedHashSet<String>();

    private final ReflectionManager reflectionManager;

    private final List<XClass> xClasses = new ArrayList<XClass>();

    public AnnotationMetadataSourceProcessorImpl(ManagedResources managedResources,
            final MetadataBuildingContextRootImpl rootMetadataBuildingContext) {
        this.rootMetadataBuildingContext = rootMetadataBuildingContext;
        this.reflectionManager = new JavaReflectionManager();

        if (CollectionHelper.isNotEmpty(managedResources.getAnnotatedPackageNames())) {
            annotatedPackages.addAll(managedResources.getAnnotatedPackageNames());
        }

        for (String className : managedResources.getAnnotatedClassNames()) {
            try {
                final Class annotatedClass = Class.forName(className);
                categorizeAnnotatedClass(annotatedClass);
            } catch (ClassNotFoundException e) {
                LOGGER.warn("ClassNotFound", e);
            }
        }

        for (Class annotatedClass : managedResources.getAnnotatedClassReferences()) {
            categorizeAnnotatedClass(annotatedClass);
        }
    }

    private void categorizeAnnotatedClass(Class annotatedClass) {
        final XClass xClass = reflectionManager.toXClass(annotatedClass);
        // categorize it, based on assumption it does not fall into multiple categories
        if (xClass.isAnnotationPresent(Converter.class)) {
            //noinspection unchecked
            //			attributeConverterManager.addAttributeConverter( annotatedClass );
        } else if (xClass.isAnnotationPresent(Entity.class) || xClass.isAnnotationPresent(MappedSuperclass.class)) {
            xClasses.add(xClass);
        } else if (xClass.isAnnotationPresent(Embeddable.class)) {
            xClasses.add(xClass);
        } else {
            LOGGER.debug("Encountered a non-categorized annotated class [%s]; ignoring", annotatedClass.getName());
        }
    }

    @SuppressWarnings("deprecation")
    private XClass toXClass(String className, ReflectionManager reflectionManager) {
        try {
            return reflectionManager.classForName(className);
        } catch (ClassLoadingException e) {
            throw new AnnotationException("Unable to load class defined in XML: " + className, e);
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
		/*rootMetadataBuildingContext.getMetadataCollector().getDatabase().adjustDefaultNamespace(
				rootMetadataBuildingContext.getBuildingOptions().getMappingDefaults().getImplicitCatalogName(),
				rootMetadataBuildingContext.getBuildingOptions().getMappingDefaults().getImplicitSchemaName()
		);*/

        AnnotationBinder.bindDefaults(rootMetadataBuildingContext);
        for (String annotatedPackage : annotatedPackages) {
            AnnotationBinder.bindPackage(annotatedPackage, rootMetadataBuildingContext);
        }
    }

    @Override
    public void processEntityHierarchies(Set<String> processedEntityNames) {
        final List<XClass> orderedClasses = orderAndFillHierarchy(xClasses);
        Map<XClass, InheritanceState> inheritanceStatePerClass = AnnotationBinder
                .buildInheritanceStates(orderedClasses, rootMetadataBuildingContext);
        for (XClass clazz : orderedClasses) {
            if (processedEntityNames.contains(clazz.getName())) {
                LOGGER.debug("Skipping annotated class processing of entity [%s], as it has already been processed",
                        clazz);
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
            while (superClass != null && !reflectionManager.equals(superClass, Object.class) && !copy
                    .contains(superClass)) {
                if (superClass.isAnnotationPresent(Entity.class) || superClass
                        .isAnnotationPresent(MappedSuperclass.class)) {
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

}
