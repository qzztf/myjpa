package cn.sexycode.mybatis.jpa.samples;

import cn.sexycode.mybatis.jpa.data.repository.support.MyJpaRepositoryFactoryBean;
import cn.sexycode.mybatis.jpa.samples.dao.UserDao;
import cn.sexycode.mybatis.jpa.samples.model.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author qinzaizhen
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = MyJpaRepositoryFactoryBean.class)
//@MapperScan("cn.sexycode.mybatis.jpa.samples.dao")
public class MyJpaSamplesApplication {
    public static void main(String[] args) {
        User user = SpringApplication.run(MyJpaSamplesApplication.class, args).getBeanFactory().getBean(UserDao.class).getOne("1");
        System.out.println("user: " +user);
    }
}
