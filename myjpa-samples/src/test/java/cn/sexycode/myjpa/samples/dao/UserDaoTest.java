package cn.sexycode.myjpa.samples.dao;

import cn.sexycode.myjpa.binding.ModelProxy;
import cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean;
import cn.sexycode.myjpa.samples.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;

//import MyjpaRepositoryFactoryBean;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaRepositories(repositoryFactoryBeanClass = MyjpaRepositoryFactoryBean.class)
//@ContextConfiguration(classes = JPAConfig.class)
//@MapperScan("cn.sexycode.mybatis.jpa.samples.dao")
public class UserDaoTest {
    @Autowired
    UserDao userDao;

    @Test
    public void testFindByName() {
        System.out.println(userDao.findAll());
    }

    @Test
    public void mybatis() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        UserDao userDao = sqlSessionFactory.openSession().getMapper(UserDao.class);
        userDao.findByFullName("111");
    }

    @Test
    public void jpaTest() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        User user = new User();
        user.setId("11");
        ModelProxy persistModel = new ModelProxy<>(user, "cn.sexycode.myjpa.samples.dao.UserDao.save");
        entityManager.persist(persistModel);

        ModelProxy findModelProxy = new ModelProxy<>("11", "cn.sexycode.myjpa.samples.dao.UserDao.findById");
        System.out.println("插入后查找: " + entityManager.find(User.class, findModelProxy));

        user.setFullName("qzz");
        ModelProxy updateModel = new ModelProxy<>(user, "UserDao.updateById");
        entityManager.merge(updateModel);
        System.out.println("更新后查找: " + entityManager.find(User.class, findModelProxy));

        ModelProxy removeModel = new ModelProxy<>(user, "UserDao.removeById");
        entityManager.remove(removeModel);

        System.out.println("删除后查找: " + entityManager.find(User.class, findModelProxy));
    }
}