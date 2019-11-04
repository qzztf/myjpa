package cn.sexycode;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MyjpaTest {
    @Test
    public void jpaTest() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User user = new User();
        user.setName("qzz");
        //以上两行为新建状态
        //托管状态
        em.persist(user);
        //事务提交或调用flush()方法后会同步到数据库
        em.getTransaction().commit();
        //根据主键获取对象
        //System.err.println(em.find(User.class,1));
        //System.err.println(em.getReference(User.class,1));
        em.close();
        entityManagerFactory.close();
    }
}
