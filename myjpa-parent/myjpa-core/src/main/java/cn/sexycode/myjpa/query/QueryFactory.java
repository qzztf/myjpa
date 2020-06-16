package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.session.Session;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author qinzaizhen
 */
public interface QueryFactory {
    <T> TypedQuery<T> createQuery(Session session, String qlString, Class<T> resultClass);

    Query createNamedQuery(Session session, String name);

    <T> TypedQuery<T> createNamedQuery(Session session, String name, Class<T> resultClass);

    Query createQuery(Session session, String qlString);

    <T> TypedQuery<T> createQuery(Session session, CriteriaQuery<T> criteriaQuery);

    CriteriaBuilder createCriteriaBuilder(Session session);
}
