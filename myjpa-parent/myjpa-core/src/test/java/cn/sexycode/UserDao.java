package cn.sexycode;

import java.util.List;

/**
 *
 */
public interface UserDao extends BaseMapper<User> {
    List<User> findByFullName(String name);

        int insert(User user);
}
