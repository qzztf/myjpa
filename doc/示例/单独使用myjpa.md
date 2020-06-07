# 单独使用Myjpa

单独使用时，使用的是jpa的api，如：`persist`、`find`、`merge`等。

1. 引入依赖

   ```xml
   <artifactId>myjpa-core</artifactId>
   <groupId>cn.sexycode</groupId>
   <version>0.0.4-SNAPSHOT</version>
   ```

2. 创建`persistence.xml`文件

   在`resources/META-INF`目标下新建`persistence.xml`文件，配置Jpa。

   ```xml
   <persistence-unit name="MyJPA" transaction-type="RESOURCE_LOCAL">
     <properties>
       <property name="myjpa.jpa.entity.method.find" value="findById"/>
     </properties>
   </persistence-unit>
   ```
   
   **这里引入了新的属性配置`myjpa.jpa.entity.method.find` ,  指定调用`find`方法时，映射为要使用的mybatis statement的语句。**
   
   *由于使用mybatis来初始化数据源，因此不需要在此文件中配置数据源*。
   
3. 创建`mybatis-config.xml`文件。
   
   默认使用此文件名，可以单独指定。通过在`persistence.xml`文件中配置property。如：
   
   ```xml
   <property name="myjpa.mybatis.config.location" value="mybatis.xml"/>
   
   ```
   
   在`mybatis-config.xml`文件中引入mapper xml文件以及数据源。
   
   ```xml
   <environments default="development">
     <environment id="development">
       <transactionManager type="JDBC"/>
       <dataSource type="POOLED">
         <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
         <property name="url" value="jdbc:mysql://localhost:3306/jpademo?useUnicode=true&amp;characterEncoding=utf-8&amp;serverTimezone=UTC"/>
         <property name="username" value="root"/>
         <property name="password" value="123456"/>
       </dataSource>
     </environment>
   </environments>
   <mappers>
     <mapper resource="UserMapper.xml"/>
   </mappers>
   ```
   
4. 定义mapper xml文件

   ```xml
   <mapper namespace="cn.sexycode.myjpa.samples.model.User">
       <insert id="insert" parameterType="cn.sexycode.myjpa.samples.model.User">
           insert into user(id,full_name,account,password) values (#{id},#{fullName}, #{account}, #{password})
       </insert>
       <update id="update" parameterType="cn.sexycode.myjpa.samples.model.User">
           update user set full_name = #{fullName} where id = #{id}
       </update>
       <select id="findByFullName" resultType="cn.sexycode.myjpa.samples.model.User">
       select * from user where id = #{id}
     </select>
       <select id="findById" resultType="cn.sexycode.myjpa.samples.model.User">
       select * from user where id = #{id}
     </select>
   </mapper>
   ```

   

4. 使用

   ```java
   public static void main(String[] args) throws IOException {
     //创建 EntityManagerFactory
     EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MyJPA");
     EntityManager em = entityManagerFactory.createEntityManager();
     em.getTransaction().begin();
     User user = new User();
     user.setId("8");
     user.setAccount("ffdfdfddd");
     user.setPassword("1111");
     user.setFullName("qzz");
     //保存user对象
     em.persist(user);
   
     //根据主键获取对象
     System.err.println(em.find(User.class,8));
   
     user.setFullName("更新的值");
     //update
     System.err.println(em.merge(user));
     //事务提交或调用flush()方法后会同步到数据库
     em.getTransaction().commit();
     em.close();
     entityManagerFactory.close();
   }
   ```

   

# 注意事项

默认jpa方法映射到mybatis mapper xml 语句id:

| JPA方法 | Mybatis 语句id | 配置                           |
| ------- | -------------- | ------------------------------ |
| find    | findById       | myjpa.jpa.entity.method.find   |
| remove  | remove         | myjpa.jpa.entity.method.remove |
| merge   | update         | myjpa.jpa.entity.method.update |
| persist | insert         | myjpa.jpa.entity.method.insert |

   

