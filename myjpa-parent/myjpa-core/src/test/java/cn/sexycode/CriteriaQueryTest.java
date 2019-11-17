package cn.sexycode;

import cn.sexycode.myjpa.DefaultBeanFactory;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public class CriteriaQueryTest {
    @Test
    public void queryTest(){
        BeanFactoryUtil.setBeanFactory(new DefaultBeanFactory());
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaQuery<User> query = em.getCriteriaBuilder().createQuery(User.class);
        query.select(query.from(User.class));
        System.out.println(em.createQuery(query).getResultList());
    }
}
