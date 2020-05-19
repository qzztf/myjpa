package cn.sexycode.myjpa.samples.dao;

import cn.sexycode.myjpa.samples.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface UserDao{
    List<User> findByFullName(String name);


//    int save(User user);
}
