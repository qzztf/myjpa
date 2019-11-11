package cn.sexycode;

import cn.sexycode.myjpa.DefaultBeanFactory;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MyjpaTest {
    @Test
    public void jpaTest() {

        BeanFactoryUtil.setBeanFactory(new DefaultBeanFactory());
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        User user = new User();
        user.setId("1");
        user.setName("qzz");
        //以上两行为新建状态
        //托管状态
        em.persist(user);
        //事务提交或调用flush()方法后会同步到数据库
//        em.getTransaction().commit();
        //根据主键获取对象
        //System.err.println(em.find(User.class,1));
        //System.err.println(em.getReference(User.class,1));
        System.out.println(((Session) em).getSession().getMapper(UserDao.class).findByFullName("qq"));
        em.close();
        entityManagerFactory.close();
    }
}
