package cn.sexycode;

import cn.sexycode.myjpa.DefaultBeanFactory;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CriteriaQueryTest {
    @Test
    public void queryTest(){
        BeanFactoryUtil.setBeanFactory(new DefaultBeanFactory());
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        query.select(userRoot);
        query.where(criteriaBuilder.equal(userRoot.get("id"),"1"));
        System.out.println(em.createQuery(query).getResultList());
    }
}
