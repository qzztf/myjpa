package cn.sexycode.myjpa.samples.dao;

import cn.sexycode.myjpa.data.repository.MyjpaRepository;
import cn.sexycode.myjpa.samples.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface UserDao extends MyjpaRepository<User, String> {
    List<User> findByFullName(String name);

//    int save(User user);
}
