package cn.sexycode.mybatis.jpa.service;

import cn.sexycode.mybatis.jpa.MyJpaException;

/**
 *
 */
public class NullServiceException extends MyJpaException {
    public final Class serviceRole;

    public NullServiceException(Class serviceRole) {
        super("Unknown service requested [" + serviceRole.getName() + "]");
        this.serviceRole = serviceRole;
    }

    public Class getServiceRole() {
        return serviceRole;
    }
}
