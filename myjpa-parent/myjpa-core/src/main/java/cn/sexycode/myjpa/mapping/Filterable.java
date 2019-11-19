package cn.sexycode.myjpa.mapping;

/**
 * Defines mapping elements to which filters may be applied.
 *
 * @author Steve Ebersole
 */
public interface Filterable {
    void addFilter(String name, String condition, boolean autoAliasInjection,
            java.util.Map<String, String> aliasTableMap, java.util.Map<String, String> aliasEntityMap);

    java.util.List getFilters();
}
