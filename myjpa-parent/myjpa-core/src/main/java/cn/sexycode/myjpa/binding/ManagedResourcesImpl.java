package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.boot.BootstrapContext;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Steve Ebersole
 */
public class ManagedResourcesImpl implements ManagedResources {
    private Set<Class> annotatedClassReferences = new LinkedHashSet<Class>();

    private Set<String> annotatedClassNames = new LinkedHashSet<String>();

    private Set<String> annotatedPackageNames = new LinkedHashSet<String>();

    public static ManagedResourcesImpl baseline(MetadataSources sources, BootstrapContext bootstrapContext) {
        final ManagedResourcesImpl impl = new ManagedResourcesImpl();

        impl.annotatedClassReferences.addAll(sources.getAnnotatedClasses());
        impl.annotatedClassNames.addAll(sources.getAnnotatedClassNames());
        impl.annotatedPackageNames.addAll(sources.getAnnotatedPackages());
        return impl;
    }

    private ManagedResourcesImpl() {
    }

    @Override
    public Collection<Class> getAnnotatedClassReferences() {
        return Collections.unmodifiableSet(annotatedClassReferences);
    }

    @Override
    public Collection<String> getAnnotatedClassNames() {
        return Collections.unmodifiableSet(annotatedClassNames);
    }

    @Override
    public Collection<String> getAnnotatedPackageNames() {
        return Collections.unmodifiableSet(annotatedPackageNames);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // package private

    void addAnnotatedClassReference(Class annotatedClassReference) {
        annotatedClassReferences.add(annotatedClassReference);
    }

    void addAnnotatedClassName(String annotatedClassName) {
        annotatedClassNames.add(annotatedClassName);
    }

    void addAnnotatedPackageName(String annotatedPackageName) {
        annotatedPackageNames.add(annotatedPackageName);
    }

}
