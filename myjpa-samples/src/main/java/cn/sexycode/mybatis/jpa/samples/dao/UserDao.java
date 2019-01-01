package cn.sexycode.mybatis.jpa.samples.dao;

import cn.sexycode.mybatis.jpa.samples.model.User;
//import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 */
public interface UserDao /*extends CrudRepository<User, String> */{
    List<User> findByName(String name);



    int insert(User user);
}
