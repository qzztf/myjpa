package cn.sexycode.myjpa.samples;

//import cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean;

import cn.sexycode.myjpa.samples.dao.UserDao;
import cn.sexycode.myjpa.samples.model.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author qinzaizhen
 */
public class MyJpaSamplesApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("app.xml");
        UserDao userDao = context.getBean(UserDao.class);
        System.out.println("user: " + userDao.findByFullName("1"));
    }
}
