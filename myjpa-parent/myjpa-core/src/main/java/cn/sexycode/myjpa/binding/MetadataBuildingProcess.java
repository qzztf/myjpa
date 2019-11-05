package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.boot.BootstrapContext;
import cn.sexycode.sql.type.BasicTypeRegistry;
import cn.sexycode.sql.type.TypeFactory;
import cn.sexycode.sql.type.TypeResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the process of of transforming a {@link org.hibernate.boot.MetadataSources}
 * reference into a {@link org.hibernate.boot.Metadata} reference.  Allows for 2 different process paradigms:<ul>
 * <li>
 * Single step : as defined by the {@link #build} method; internally leverages the 2-step paradigm
 * </li>
 * <li>
 * Two step : a first step coordinates resource scanning and some other preparation work; a second step
 * builds the {@link org.hibernate.boot.Metadata}.  A hugely important distinction in the need for the
 * steps is that the first phase should strive to not load user entity/component classes so that we can still
 * perform enhancement on them later.  This approach caters to the 2-phase bootstrap we use in regards
 * to WildFly Hibernate-JPA integration.  The first step is defined by {@link #prepare} which returns
 * a {@link ManagedResources} instance.  The second step is defined by calling
 * {@link #complete}
 * </li>
 * </ul>
 */
public class MetadataBuildingProcess {

    /**
     * Unified single phase for MetadataSources->Metadata process
     *
     * @param sources The MetadataSources
     * @param options The building options
     * @return The built Metadata
     */
    public static Metadata build(
			final MetadataSources sources,
            final BootstrapContext bootstrapContext,
            final MetadataBuildingOptions options) {
        return complete( prepare( sources, bootstrapContext ), bootstrapContext, options );
    }

    /**
     * First step of 2-phase for MetadataSources->Metadata process
     *
     * @param sources The MetadataSources
     * @param options The building options
     * @return Token/memento representing all known users resources (classes, packages, mapping files, etc).
     */
    public static ManagedResources prepare(final MetadataSources sources, BootstrapContext bootstrapContext) {
        final ManagedResourcesImpl managedResources = ManagedResourcesImpl.baseline(sources, bootstrapContext);
        ScanningCoordinator.INSTANCE.coordinateScan(managedResources,  bootstrapContext );
        return managedResources;
    }

    /**
     * Second step of 2-phase for MetadataSources->Metadata process
     *
     * @param managedResources The token/memento from 1st phase
     * @param options          The building options
     * @return Token/memento representing all known users resources (classes, packages, mapping files, etc).
     */
    public static Metadata complete(
            final ManagedResources managedResources,
            final BootstrapContext bootstrapContext,
            final MetadataBuildingOptions options) {
        final BasicTypeRegistry basicTypeRegistry = handleTypes(options);
        final Set<String> processedEntityNames = new HashSet<String>();
        final InFlightMetadataCollectorImpl metadataCollector = new InFlightMetadataCollectorImpl(options,
                new TypeResolver(basicTypeRegistry, new TypeFactory()));
        final MetadataBuildingContextRootImpl rootMetadataBuildingContext = new MetadataBuildingContextRootImpl(options,
                metadataCollector);
        final MetadataSourceProcessor processor = new SimpleMetadataSourceProcessorImpl() {

            private final AnnotationMetadataSourceProcessorImpl annotationProcessor = new AnnotationMetadataSourceProcessorImpl(
                    managedResources, rootMetadataBuildingContext);

            @Override
            public void prepare() {
                annotationProcessor.prepare();
            }

            @Override
            public void processTypeDefinitions() {
                annotationProcessor.processTypeDefinitions();
            }

            @Override
            public void processQueryRenames() {
                annotationProcessor.processQueryRenames();
            }

            @Override
            public void processNamedQueries() {
                annotationProcessor.processNamedQueries();
            }

            @Override
            public void processAuxiliaryDatabaseObjectDefinitions() {
                annotationProcessor.processAuxiliaryDatabaseObjectDefinitions();
            }

            @Override
            public void processIdentifierGenerators() {
                annotationProcessor.processIdentifierGenerators();
            }

            @Override
            public void processFilterDefinitions() {
                annotationProcessor.processFilterDefinitions();
            }

            @Override
            public void processFetchProfiles() {
                annotationProcessor.processFetchProfiles();
            }

            @Override
            public void processResultSetMappings() {
                annotationProcessor.processResultSetMappings();
            }

            @Override
            public void finishUp() {
                annotationProcessor.finishUp();
            }
        };

        processor.prepare();

        processor.processTypeDefinitions();
        processor.processQueryRenames();
        processor.processAuxiliaryDatabaseObjectDefinitions();

        processor.processIdentifierGenerators();
        processor.processFilterDefinitions();
        processor.processFetchProfiles();

        processor.prepareForEntityHierarchyProcessing();
        processor.processEntityHierarchies(processedEntityNames);
        processor.postProcessEntityHierarchies();

        processor.processResultSetMappings();
        processor.processNamedQueries();

        processor.finishUp();
        return metadataCollector.buildMetadataInstance(rootMetadataBuildingContext);
    }

    private static BasicTypeRegistry handleTypes(MetadataBuildingOptions options) {

        // ultimately this needs to change a little bit to account for HHH-7792
        final BasicTypeRegistry basicTypeRegistry = new BasicTypeRegistry();

        // add explicit application registered types
        for (BasicTypeRegistration basicTypeRegistration : options.getBasicTypeRegistrations()) {
            basicTypeRegistry
                    .register(basicTypeRegistration.getBasicType(), basicTypeRegistration.getRegistrationKeys());
        }

        return basicTypeRegistry;
    }

}
