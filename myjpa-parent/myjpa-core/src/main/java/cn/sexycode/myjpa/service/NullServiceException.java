package cn.sexycode.myjpa.service;

import cn.sexycode.myjpa.MyjpaException;

/**
 *
 */
public class NullServiceException extends MyjpaException {
    public final Class serviceRole;

    public NullServiceException(Class serviceRole) {
        super("Unknown service requested [" + serviceRole.getName() + "]");
        this.serviceRole = serviceRole;
    }

    public Class getServiceRole() {
        return serviceRole;
    }
}
