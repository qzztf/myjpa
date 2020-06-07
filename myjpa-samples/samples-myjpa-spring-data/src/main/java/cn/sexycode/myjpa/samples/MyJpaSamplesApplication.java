package cn.sexycode.myjpa.samples;

//import cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean;

import cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean;
import cn.sexycode.myjpa.samples.dao.UserDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import org.mybatis.spring.annotation.MapperScan;

/**
 *
 * @author qinzaizhen
 */
@EnableJpaRepositories(repositoryFactoryBeanClass = MyjpaRepositoryFactoryBean.class)
public class MyJpaSamplesApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("app.xml");
        UserDao userDao = context.getBean(UserDao.class);
//        System.out.println("user: " + userDao.findByFullName("1"));
        System.out.println("user page: " + userDao.findUserByFullName("1", PageRequest.of(1,10)));
    }
}
