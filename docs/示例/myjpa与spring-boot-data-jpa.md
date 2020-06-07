1. 引入依赖

   ```xml
   <dependency>
       <groupId>cn.sexycode</groupId>
       <artifactId>myjpa-data-spring-boot-starter</artifactId>
       <version>0.0.4-SNAPSHOT</version>
   </dependency>
   <dependency>
       <groupId>org.mybatis.spring.boot</groupId>
       <artifactId>mybatis-spring-boot-starter</artifactId>
       <version>2.1.3</version>
   </dependency>
   ```

2. 配置数据源和mapper扫描

   ```yaml
   mybatis:
     mapper-locations: UserMapper.xml
   
   spring:
     datasource:
       password:
         zxcf123456
       username: root
       url: jdbc:mysql://localhost:3306/jpademo?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false
   ```

   ```java
   @MapperScan("cn.sexycode.myjpa.samples.dao")
   ```

3. 修改dao， 继承自`MyjpaRepository`

   ```java
   public interface UserDao extends MyjpaRepository<User, String> {
       List<User> findByFullName(String name);
   }
   ```

4. 使用

   ```java
   @SpringBootApplication(scanBasePackages = "cn.sexycode.myjpa.samples")
   @MapperScan("cn.sexycode.myjpa.samples.dao")
   public class MyJpaSamplesApplication {
     public static void main(String[] args) {
       ConfigurableApplicationContext context = SpringApplication.run(MyJpaSamplesApplication.class);
   
       UserDao userDao = context.getBean(UserDao.class);
       System.out.println("user: " + userDao.findByFullName("1"));
   		//此方法来自 spring data jpa
       System.out.println("user: " + userDao.findById("1"));
     }
   }
   ```

   使用此方式时，spring已经为我们提供了超多的接口，加上IDEA的智能提示，还能由我们自己控制sql，感觉超级爽。