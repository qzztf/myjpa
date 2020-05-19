package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.binding.ModelProxy;
import cn.sexycode.util.core.str.StringUtils;
import org.apache.ibatis.session.SqlSession;

import javax.persistence.PersistenceException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qzz
 */
public class SessionAdaptor {
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

    public Object execute(String method, Object... param) {
        if (param != null && param.length > 0) {
            try {
                Object entity = param[0];
                Method mappingMethod = methodMapping.get(method);
                if (entity.getClass().isAssignableFrom(ModelProxy.class)) {
                    ModelProxy modelProxy = (ModelProxy) entity;
                    return mappingMethod.invoke(session.getSession(), modelProxy.getStatement(), modelProxy.getModel());
                } else {
                    return mappingMethod
                            .invoke(session.getSession(), StringUtils.join(".", new String[]{entity.getClass().getCanonicalName(), mappingMethod.getName()}), entity);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new PersistenceException(e);
            }
        }
        return param;
    }
}
