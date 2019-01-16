package cn.sexycode.mybatis.jpa.samples.dao;

import cn.sexycode.mybatis.jpa.data.repository.MyJpaRepository;
import cn.sexycode.mybatis.jpa.samples.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface UserDao extends MyJpaRepository<User, String> {
    List<User> findByFullName(String name);


//    int save(User user);
}
