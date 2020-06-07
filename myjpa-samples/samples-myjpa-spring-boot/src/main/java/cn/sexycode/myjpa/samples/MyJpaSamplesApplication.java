package cn.sexycode.myjpa.samples;

import cn.sexycode.myjpa.samples.dao.UserDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qzz
 */
@SpringBootApplication(scanBasePackages = "cn.sexycode.myjpa.samples")
@MapperScan("cn.sexycode.myjpa.samples.dao")
public class MyJpaSamplesApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MyJpaSamplesApplication.class);

        UserDao userDao = context.getBean(UserDao.class);
        System.out.println("user: " + userDao.findByFullName("1"));
    }
}
