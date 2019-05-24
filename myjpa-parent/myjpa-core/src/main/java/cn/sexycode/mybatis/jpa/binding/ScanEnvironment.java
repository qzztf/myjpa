package cn.sexycode.mybatis.jpa.binding;

import java.net.URL;
import java.util.List;

/**
 * Describes the environment in which the scan will occur.
 * <p/>
 * Note that much of this comes from the PU in JPA sense.  This is
 * intended as an abstraction over the PU in JPA cases, as well as a
 * delegate allowing usage in non-JPA cases.  With the planned move
 * to unify the cfg.xml and persistence.xml schemas (like we are doing
 * with hbm.xml and orm.xml) this becomes less needed (at least parts
 * of it).
 * <p/>
 * After unification, I think the biggest difference is that we will
 * not need to pass ScanEnvironment into the MetadataSources/MetadataBuilder
 * while for the time being we will need to.
 */
public interface ScanEnvironment {
    /**
     * Returns the root URL for scanning.  Can be {@code null}, indicating that
     * no root URL scanning should be done (aka, if maybe a root URL is not known).
     *
     * @return The root URL
     */
    URL getRootUrl();

    /**
     * Returns any non-root URLs for scanning.  Can be null/empty to indicate
     * that no non-root URL scanning should be done.
     *
     * @return The non-root URLs
     */
    List<URL> getNonRootUrls();

    /**
     * Returns any classes which are explicitly listed as part of the
     * "persistence unit".
     *
     * @return The explicitly listed classes
     */
    List<String> getExplicitlyListedClassNames();

}
