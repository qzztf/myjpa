package cn.sexycode.mybatis.jpa.samples.dao;

import cn.sexycode.mybatis.jpa.samples.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface UserDao extends JpaRepository<User, String> {
    List<User> findByName(String name);


//    int save(User user);
}
