package cn.sexycode.myjpa.samples.dao;

import cn.sexycode.myjpa.data.repository.MyjpaRepository;
import cn.sexycode.myjpa.samples.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface UserDao extends MyjpaRepository<User, String> {
    List<User> findByFullName(String name);

    Page<User> findUserByFullName(@Param("name") String name, Pageable p);
//    int save(User user);
}
