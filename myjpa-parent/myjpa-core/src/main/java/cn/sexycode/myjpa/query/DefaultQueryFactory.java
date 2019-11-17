package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.query.criteria.internal.compile.CompilableCriteria;
import cn.sexycode.myjpa.query.criteria.internal.compile.CriteriaCompiler;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.util.core.object.ObjectUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
        MappedStatement mappedStatement = null;
        try {
            mappedStatement = session.getConfiguration().getMappedStatement(name);
        } catch (Exception e) {
            LOGGER.debug("获取MappedStatement失败", e);
            return null;
        }
        if (ObjectUtils.isEmpty(mappedStatement)) {
            return null;
        }
        return new MybatisNamedQueryImpl(session, name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(Session session, String name, Class<T> resultClass) {
        MappedStatement mappedStatement = session.getConfiguration().getMappedStatement(name);
        if (ObjectUtils.isEmpty(mappedStatement)) {
            return null;
        }
        return new MybatisNamedQueryImpl<>(session, name, resultClass);
    }

    @Override
    public Query createQuery(Session session, String qlString) {
        return new MybatisQueryImpl(session, qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(Session session, CriteriaQuery<T> criteriaQuery) {
        CriteriaCompiler criteriaCompiler = new CriteriaCompiler(session);
        return (TypedQuery<T>) criteriaCompiler.compile( (CompilableCriteria) criteriaQuery );
    }
}
