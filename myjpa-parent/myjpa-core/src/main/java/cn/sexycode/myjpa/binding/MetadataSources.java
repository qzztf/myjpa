package cn.sexycode.myjpa.binding;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Entry point into working with sources of metadata information (mapping XML, annotations).   Tell Hibernate
 * about sources and then call {@link #buildMetadata()}, or use {@link #getMetadataBuilder()} to customize
 * how sources are processed (naming strategies, etc).
 */
public class MetadataSources implements Serializable {

    private LinkedHashSet<Class<?>> annotatedClasses = new LinkedHashSet<Class<?>>();

    private LinkedHashSet<String> annotatedClassNames = new LinkedHashSet<String>();

    private LinkedHashSet<String> annotatedPackages = new LinkedHashSet<String>();

    public Collection<String> getAnnotatedPackages() {
        return annotatedPackages;
    }

    public Collection<Class<?>> getAnnotatedClasses() {
        return annotatedClasses;
    }

    public Collection<String> getAnnotatedClassNames() {
        return annotatedClassNames;
    }

    /**
     * Read metadata from the annotations attached to the given class.
     *
     * @param annotatedClass The class containing annotations
     * @return this (for method chaining)
     */
    public MetadataSources addAnnotatedClass(Class annotatedClass) {
        annotatedClasses.add(annotatedClass);
        return this;
    }

    /**
     * Read metadata from the annotations attached to the given class.  The important
     * distinction here is that the {@link Class} will not be accessed until later
     * which is important for on-the-fly bytecode-enhancement
     *
     * @param annotatedClassName The name of a class containing annotations
     * @return this (for method chaining)
     */
    public MetadataSources addAnnotatedClassName(String annotatedClassName) {
        annotatedClassNames.add(annotatedClassName);
        return this;
    }

    /**
     * Read package-level metadata.
     *
     * @param packageName java package name without trailing '.', cannot be {@code null}
     * @return this (for method chaining)
     */
    public MetadataSources addPackage(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("The specified package name cannot be null");
        }

        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }

        annotatedPackages.add(packageName);

        return this;
    }

    /**
     * Read package-level metadata.
     *
     * @param packageRef Java Package reference
     * @return this (for method chaining)
     */
    public MetadataSources addPackage(Package packageRef) {
        annotatedPackages.add(packageRef.getName());
        return this;
    }

}
