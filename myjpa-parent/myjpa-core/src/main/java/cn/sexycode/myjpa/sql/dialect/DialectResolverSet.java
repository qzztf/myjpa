package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.DialectResolutionInfo;
import cn.sexycode.myjpa.sql.dialect.DialectResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link cn.sexycode.myjpa.sql.dialect.DialectResolver} implementation which coordinates resolution by delegating to sub-resolvers.
 *
 */
public class DialectResolverSet implements cn.sexycode.myjpa.sql.dialect.DialectResolver {

    private List<cn.sexycode.myjpa.sql.dialect.DialectResolver> resolvers;

    public DialectResolverSet() {
        this(new ArrayList<cn.sexycode.myjpa.sql.dialect.DialectResolver>());
    }

    public DialectResolverSet(List<cn.sexycode.myjpa.sql.dialect.DialectResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public DialectResolverSet(cn.sexycode.myjpa.sql.dialect.DialectResolver... resolvers) {
        this(Arrays.asList(resolvers));
    }

    @Override
    public cn.sexycode.myjpa.sql.dialect.Dialect resolveDialect(DialectResolutionInfo info) {
        for (cn.sexycode.myjpa.sql.dialect.DialectResolver resolver : resolvers) {
            try {
                final Dialect dialect = resolver.resolveDialect(info);
                if (dialect != null) {
                    return dialect;
                }
            } catch (Exception e) {
            }
        }

        return null;
    }

    /**
     * Add a resolver at the end of the underlying resolver list.  The resolver added by this method is at lower
     * priority than any other existing resolvers.
     *
     * @param resolver The resolver to add.
     */
    public void addResolver(cn.sexycode.myjpa.sql.dialect.DialectResolver resolver) {
        resolvers.add(resolver);
    }

    /**
     * Add a resolver at the beginning of the underlying resolver list.  The resolver added by this method is at higher
     * priority than any other existing resolvers.
     *
     * @param resolver The resolver to add.
     */
    public void addResolverAtFirst(DialectResolver resolver) {
        resolvers.add(0, resolver);
    }
}
