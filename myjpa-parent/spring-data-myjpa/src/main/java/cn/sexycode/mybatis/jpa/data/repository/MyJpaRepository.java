package cn.sexycode.mybatis.jpa.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author qinzaizhen
 */
@NoRepositoryBean
public interface MyJpaRepository<T, ID> extends JpaRepository<T, ID> {
}
