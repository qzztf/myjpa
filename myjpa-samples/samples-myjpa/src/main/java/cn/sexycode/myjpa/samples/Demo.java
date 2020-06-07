package cn.sexycode.myjpa.samples;

import cn.sexycode.myjpa.samples.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

/**
 * @author qinzaizhen
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User user = new User();
        user.setId("8");
        user.setAccount("ffdfdfddd");
        user.setPassword("1111");
        user.setFullName("qzz");
        //以上两行为新建状态
        //托管状态
        em.persist(user);

        //根据主键获取对象
        System.err.println(em.find(User.class,1));
        //System.err.println(em.getReference(User.class,1));

        user.setFullName("更新的值");
        System.err.println(em.merge(user));
        //事务提交或调用flush()方法后会同步到数据库
        em.getTransaction().commit();
        em.close();
        entityManagerFactory.close();
    }
}
