package cn.sexycode.myjpa.binding;

import javax.persistence.spi.PersistenceUnitInfo;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of ScanEnvironment leveraging a JPA deployment descriptor.
 */
public class StandardScanEnvironmentImpl implements ScanEnvironment {
    private final PersistenceUnitInfo persistenceUnitInfo;

    private final List<String> explicitlyListedClassNames;

    public StandardScanEnvironmentImpl(PersistenceUnitInfo persistenceUnitInfo) {
        this.persistenceUnitInfo = persistenceUnitInfo;

        this.explicitlyListedClassNames = persistenceUnitInfo.getManagedClassNames() == null
                ? Collections.emptyList()
                : persistenceUnitInfo.getManagedClassNames();
    }

    @Override
    public URL getRootUrl() {
        return persistenceUnitInfo.getPersistenceUnitRootUrl();
    }

    @Override
    public List<URL> getNonRootUrls() {
        return persistenceUnitInfo.getJarFileUrls();
    }

    @Override
    public List<String> getExplicitlyListedClassNames() {
        return explicitlyListedClassNames;
    }

}
