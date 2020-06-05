package cn.sexycode.myjpa.data.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author qinzaizhen
 */
@NoRepositoryBean
public interface MyjpaRepository<T, ID> extends JpaRepositoryImplementation<T, ID> {
}
