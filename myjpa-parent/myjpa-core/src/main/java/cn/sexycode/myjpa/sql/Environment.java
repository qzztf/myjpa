package cn.sexycode.myjpa.sql;

import java.util.Properties;

/**
 * @author qinzaizhen
 */
public interface Environment {
    String DIALECT = "mysql.dialect";
    String MULTI_TENANT = "mysql.multiTenancy";

    /**
     * @return
     */
    static Environment getEnvironment() {
        return null;
    }

    /**
     * @return
     */
    Properties getProperties();

}
