package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.binding.ModelProxy;
import cn.sexycode.util.core.str.StringUtils;
import cn.sexycode.util.core.object.ObjectUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qzz
 */
public class SessionAdaptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAdaptor.class);
    private final Session session;

    private static Map<String, Method> methodMapping = new HashMap<>();

    {
        try {
            methodMapping.put("persist", SqlSession.class.getMethod("insert", String.class, Object.class));
            methodMapping.put("merge", SqlSession.class.getMethod("update", String.class, Object.class));
            methodMapping.put("remove", SqlSession.class.getMethod("delete", String.class, Object.class));
            methodMapping.put("find", SqlSession.class.getMethod("selectOne", String.class, Object.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public SessionAdaptor(Session session) {
        this.session = session;
    }

    public Object execute(String method, Object param) {
        if (!ObjectUtils.isEmpty(param)) {
            try {
                Method invokeMethod = methodMapping.get(method);
                if (param.getClass().isAssignableFrom(ModelProxy.class)) {
                    ModelProxy modelProxy = (ModelProxy) param;
                    return invokeMethod.invoke(session, modelProxy.getStatement(), modelProxy.getModel());
                } else {
                    String statementId = param.getClass().getCanonicalName() + "." + method;
                    MappedStatement mappedStatement = null;
                    try {
                        mappedStatement = session.getConfiguration() .getMappedStatement(statementId);
                    } catch (Exception e) {
                        // ignore;
                        LOGGER.debug("获取MappedStatement失败", e);
                    }
                    if (!ObjectUtils.isEmpty(mappedStatement)){
                        return invokeMethod.invoke(session, statementId, param);
                    }else {
                        statementId = param.getClass().getCanonicalName() + "." + invokeMethod.getName();
                        return invokeMethod.invoke(session, statementId, param);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PersistenceException(e);
            }
        }
        return param;
    }
}
