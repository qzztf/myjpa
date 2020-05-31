package cn.sexycode.myjpa.data.repository.support;

import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author qinzaizhen
 */
public class MyjpaRepositoryFactoryBean extends JpaRepositoryFactoryBean {
    private final Class repositoryInterface;
    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public MyjpaRepositoryFactoryBean(Class repositoryInterface) {
        super(repositoryInterface);
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new MyJpaRepositoryFactory(entityManager);
    }


    private class MyJpaRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        private final EntityManager em;

        private final QueryExtractor extractor;

        public MyJpaRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
            this.extractor = PersistenceProvider.fromEntityManager(em);
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
            return new MyjpaRepositoryImpl<T, I>((Class<T>) information.getDomainType(), em, repositoryInterface);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return MyjpaRepositoryImpl.class;
        }

        @Override
        protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key,
                EvaluationContextProvider evaluationContextProvider) {
            return Optional.of(Optional
                    .ofNullable(MyjpaQueryLookupStrategy.create(em, key, extractor, evaluationContextProvider))
                    .orElse(JpaQueryLookupStrategy.create(em, key, extractor, evaluationContextProvider)));
        }
    }

}
