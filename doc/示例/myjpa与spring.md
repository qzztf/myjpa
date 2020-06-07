我们更多的是结合spring来使用myjpa.

在spring 与mybatis的结合中，大家都很熟悉。配置`SqlSessionFactoryBean`和mapper扫描，就可以省去mybatis配置文件。

同样的，而这里我们也可以采取此种方式，由spring去初始化`SqlSessionFactoryBean`和mapper扫描。

另外一种方式去类似直接使用myjpa，通过mybatis的配置文件来初始化`SqlSessionFactoryBean`和mapper。

两种方式选其一。

1.  引入依赖

   ```xml
   <dependency>
     <groupId>cn.sexycode</groupId>
     <artifactId>spring-orm-myjpa</artifactId>
     <version>0.0.4-SNAPSHOT</version>
   </dependency>
   <dependency>
     <groupId>org.mybatis</groupId>
     <artifactId>mybatis-spring</artifactId>
     <version>2.0.3</version>
   </dependency>
   ```

2. 配置实体管理器`LocalContainerEntityManagerFactoryBean`

   ```xml
   <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
     <property name="dataSource" ref="dataSource" />
     <!--实体目录-->
     <property name="packagesToScan" value="cn.sexycode.myjpa.samples.model" />
     <property name="jpaVendorAdapter">
       <bean class="cn.sexycode.myjpa.orm.vendor.MyjpaVendorAdapter">
         <property name="generateDdl" value="false" />
         <property name="database" value="MYSQL" />
         <property name="databasePlatform" value="cn.sexycode.sql.dialect.MySQLDialect" />
         <!--这个属性当使用spring来初始化sqlSessionFactory使用-->
         <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
         <!-- <property name="showSql" value="true" /> -->
       </bean>
     </property>
     <property name="jpaDialect">
       <bean class="cn.sexycode.myjpa.orm.vendor.MyjpaDialect" />
     </property>
     <property name="jpaPropertyMap">
       <map>
         <!--直接由Mybatis的配置初始化sqlSessionFactory时使用-->
         <entry key="myjpa.mybatis.config.location" value="mybatis-config.xml" />
       </map>
     </property>
   </bean>
   ```

3. 配置数据源，事务管理器

   ```xml
   <!-- 事务管理器 -->
   <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
     <property name="entityManagerFactory" ref="entityManagerFactory"/>
   </bean>
   <!-- 数据源 -->
   <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
     <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
     <property name="url" value="jdbc:mysql://localhost:3306/jpademo?serverTimezone=Asia/Shanghai&amp;useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false" />
     <property name="username" value="root" />
     <property name="password" value="zxcf123456" />
     <property name="loginTimeout" value="60"/>
     <property name="minIdle" value="5"/>
     <property name="maxWait" value="60000"/>
   </bean>
   
   ```
   
4.    配置SqlSessionFactoryBean和mapper扫描（由spring初始化sqlSessionFactory时配置这两个）

      ```xml
      <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="cn.sexycode.myjpa.samples" />
        <property name="sqlSessionFactoryBeanName" value="entityManagerFactory"/>
      </bean>
      <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="configLocation" value="mybatis-config.xml"/>
      </bean>
      ```

5.  model和mapper接口，mapper xml文件

    Model

    ```java
    @Entity
    @Table
    @Data
    public class User {
        @Id
        @GeneratedValue
        private String id;
    
        @Column
        private String fullName;
    }
    ```

    mapper接口

    ```java
    public interface UserDao {
        List<User> findByFullName(String name);
    }
    ```

    xml文件

    ```xml
    <mapper namespace="cn.sexycode.myjpa.samples.dao.UserDao">
        <insert id="save" parameterType="cn.sexycode.myjpa.samples.model.User">
            insert into user(id,full_name) values (#{id},#{fullName})
        </insert>
        <select id="findByFullName" resultType="cn.sexycode.myjpa.samples.model.User">
        select * from user where id = #{id}
      </select>
    </mapper>
    ```

6.  myjpa的`BeanFactoryAdapter`

    由于引入spring，则BeanFactory使用spring的，配置此适配器即可。

    ```xml
    <bean class="cn.sexycode.myjpa.spring.BeanFactoryAdapter"/>
    ```

    

    

7.  使用

    ```java
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("app.xml");
    UserDao userDao = context.getBean(UserDao.class);
    System.out.println("user: " + userDao.findByFullName("1"));
    ```

    使用起来和直接使用mybatis居然这么相像。
