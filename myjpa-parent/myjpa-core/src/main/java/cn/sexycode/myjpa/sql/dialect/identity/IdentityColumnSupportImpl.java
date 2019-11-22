package cn.sexycode.myjpa.sql.dialect.identity;

import cn.sexycode.myjpa.sql.dialect.identity.IdentityColumnSupport;
import cn.sexycode.myjpa.sql.mapping.MappingException;

/**
 */
public class IdentityColumnSupportImpl implements IdentityColumnSupport {

    @Override
    public boolean supportsIdentityColumns() {
        return false;
    }

    @Override
    public boolean supportsInsertSelectIdentity() {
        return false;
    }

    @Override
    public boolean hasDataTypeInIdentityColumn() {
        return true;
    }

    @Override
    public String appendIdentitySelectToInsert(String insertString) {
        return insertString;
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type) throws MappingException {
        throw new MappingException(getClass().getName() + " does not support identity key generation");
    }

    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        throw new MappingException(getClass().getName() + " does not support identity key generation");
    }

    @Override
    public String getIdentityInsertString() {
        return null;
    }

}
