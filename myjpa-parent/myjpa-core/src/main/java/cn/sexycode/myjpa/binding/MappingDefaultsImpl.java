package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.AvailableSettings;
import cn.sexycode.util.core.service.StandardServiceRegistry;

public class MappingDefaultsImpl implements MappingDefaults {
    private String implicitSchemaName;

    private String implicitCatalogName;

    private boolean implicitlyQuoteIdentifiers;

    public MappingDefaultsImpl(StandardServiceRegistry serviceRegistry) {

        this.implicitSchemaName = AvailableSettings.DEFAULT_SCHEMA;

        this.implicitCatalogName = AvailableSettings.DEFAULT_CATALOG;

        this.implicitlyQuoteIdentifiers = true;

    }

    @Override
    public String getImplicitSchemaName() {
        return implicitSchemaName;
    }

    @Override
    public String getImplicitCatalogName() {
        return implicitCatalogName;
    }

    @Override
    public boolean shouldImplicitlyQuoteIdentifiers() {
        return implicitlyQuoteIdentifiers;
    }

    @Override
    public String getImplicitIdColumnName() {
        return DEFAULT_IDENTIFIER_COLUMN_NAME;
    }

    @Override
    public String getImplicitTenantIdColumnName() {
        return DEFAULT_TENANT_IDENTIFIER_COLUMN_NAME;
    }

    @Override
    public String getImplicitDiscriminatorColumnName() {
        return DEFAULT_DISCRIMINATOR_COLUMN_NAME;
    }

    @Override
    public String getImplicitPackageName() {
        return null;
    }

    @Override
    public boolean isAutoImportEnabled() {
        return true;
    }

    @Override
    public String getImplicitCascadeStyleName() {
        return DEFAULT_CASCADE_NAME;
    }

    @Override
    public String getImplicitPropertyAccessorName() {
        return DEFAULT_PROPERTY_ACCESS_NAME;
    }

    @Override
    public boolean areEntitiesImplicitlyLazy() {
        // for now, just hard-code
        return false;
    }

    @Override
    public boolean areCollectionsImplicitlyLazy() {
        // for now, just hard-code
        return true;
    }

}