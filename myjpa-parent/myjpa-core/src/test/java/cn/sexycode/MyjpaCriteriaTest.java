package cn.sexycode;

import cn.sexycode.myjpa.DefaultBeanFactory;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class MyjpaCriteriaTest {
    @Test
    public void jpaTest() {

        BeanFactoryUtil.setBeanFactory(new DefaultBeanFactory());
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> from = query.from(User.class);
        query.where( criteriaBuilder.equal(from.get("id"), "1"));
        TypedQuery<User> userTypedQuery = em.createQuery(query);
        List<User> resultList = userTypedQuery.getResultList();
        System.out.println(resultList);
        em.close();
        entityManagerFactory.close();
    }
}
