Spring-data-jpa是一种很方便的使用jpa的方式。myjpa也做了部分适配，目前还不支持 criteria。在spring集成的基础上加上一些配置即可。

1. 引入依赖

   ```xml
   <dependency>
     <groupId>cn.sexycode</groupId>
     <artifactId>myjpa-core</artifactId>
     <version>0.0.4-SNAPSHOT</version>
   </dependency>
   <dependency>
     <groupId>cn.sexycode</groupId>
     <artifactId>spring-data-myjpa</artifactId>
     <version>0.0.4-SNAPSHOT</version>
   </dependency>
   <dependency>
     <groupId>org.mybatis</groupId>
     <artifactId>mybatis-spring</artifactId>
     <version>2.0.3</version>
   </dependency>
   ```

2. 配置reporities扫描

   ```xml
   <jpa:repositories base-package="cn.sexycode.myjpa.samples.dao" factory-class="cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean" />
   ```

   配置为使用myjpa中的`MyjpaRepositoryFactoryBean`，此类继承了`JpaRepositoryFactoryBean`，做了一些额外操作。

3. 修改dao，继承自`MyjpaRepository`接口

   ```java
   public interface UserDao extends MyjpaRepository<User, String> {
       List<User> findByFullName(String name);
   
       Page<User> findUserByFullName(@Param("name") String name, Pageable p);
   }
   ```

   这样配置就可以使用了。
   
   ```java
   public static void main(String[] args) {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("app.xml");
    UserDao userDao = context.getBean(UserDao.class);
    System.out.println("user: " + userDao.findByFullName("1"));
    }
   ```

4.  如果需要分页的话，需要额外配置一个mybatis的插件。

   ```xml
   <plugins>
     <plugin interceptor="cn.sexycode.myjpa.data.mybatis.PagePluginImpl">
     </plugin>
   </plugins>
   ```

5. dao的方法需要加上spring data的`Pageable`参数和`Page`返回值。

   ```java
   Page<User> findUserByFullName(@Param("name") String name, Pageable p);
   ```
6. 计数的sql需要在xml中定义，sql id 是对应的查询语句的id 加上后缀`Count`, 此后缀可以更改，通过属性配置`myjpa.mybatis.query.count.suffix`， 此配置全局生效。目前需要自己编写计数的sql，后面会自动生成，也会支持自己编写，自己编写的优先生效，方便sql优化。

