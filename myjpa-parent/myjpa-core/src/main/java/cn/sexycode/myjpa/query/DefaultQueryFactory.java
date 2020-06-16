package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.AvailableSettings;
import cn.sexycode.myjpa.query.criteria.MybatisCriteriaBuilder;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.util.core.object.ObjectUtils;
import cn.sexycode.util.core.properties.PropertiesUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author qinzaizhen
 */
public class DefaultQueryFactory implements QueryFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultQueryFactory.class);

    @Override
    public <T> TypedQuery<T> createQuery(Session session, String qlString, Class<T> resultClass) {
        return new MybatisQueryImpl<>(session, qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(Session session, String name) {
        return  createNamedQuery(session, name, null);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(Session session, String name, Class<T> resultClass) {
        MappedStatement mappedStatement = session.getConfiguration().getMappedStatement(name);
        if (ObjectUtils.isEmpty(mappedStatement)) {
            return null;
        }
        return name.endsWith(PropertiesUtil
                .getString(AvailableSettings.MYBATIS_QUERY_COUNT_SUFFIX, session.getProperties(),
                        AvailableSettings.Defaults.DEFAULT_MYBATIS_QUERY_COUNT_SUFFIX)) ? new MybatisNamedCountQueryImpl<>(session,name,resultClass) :
         new MybatisNamedQueryImpl<>(session, name, resultClass);
    }

    @Override
    public Query createQuery(Session session, String qlString) {
        return new MybatisQueryImpl(session, qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(Session session, CriteriaQuery<T> criteriaQuery) {
        return null;
    }

    @Override
    public CriteriaBuilder createCriteriaBuilder(Session session) {
        return new MybatisCriteriaBuilder(session);
    }
}
