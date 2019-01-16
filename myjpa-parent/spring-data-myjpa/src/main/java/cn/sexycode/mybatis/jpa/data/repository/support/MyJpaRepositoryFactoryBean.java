package cn.sexycode.mybatis.jpa.data.repository.support;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author qinzaizhen
 */
public class MyJpaRepositoryFactoryBean extends JpaRepositoryFactoryBean {
    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public MyJpaRepositoryFactoryBean(Class repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new MyJpaRepositoryFactory(entityManager);
    }

    private static class MyJpaRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        private final EntityManager em;

        public MyJpaRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
            return new MyJpaRepositoryImpl<T, I>(information.getDomainType(), em);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return MyJpaRepositoryImpl.class;
        }
    }

}
