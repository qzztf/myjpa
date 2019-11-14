package cn.sexycode.myjpa.data.repository.support;

import cn.sexycode.myjpa.binding.ModelProxy;
import cn.sexycode.myjpa.data.repository.MyjpaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author qinzaizhen
 */
public class MyjpaRepositoryImpl<T, ID> implements JpaSpecificationExecutor<T>, MyjpaRepository<T, ID> {
    private final EntityManager em;
    private final PersistenceProvider provider;
    private final Class<T> domainClass;
    private final Class repositoryInterface;

    public MyjpaRepositoryImpl(Class<T> domainClass, EntityManager em, Class repositoryInterface) {
        this.em = em;
        this.domainClass = domainClass;
        this.repositoryInterface = repositoryInterface;
        this.provider = PersistenceProvider.fromEntityManager(this.em);
    }


    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public List<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        ModelProxy findModelProxy = new ModelProxy<>(entity, repositoryInterface.getCanonicalName() + ".save");
        em.persist(findModelProxy);
        return entity;
    }

    @Transactional
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        ModelProxy findModelProxy = new ModelProxy<>(id, repositoryInterface.getCanonicalName() + ".findById");
        return Optional.ofNullable(em.find(domainClass, findModelProxy));
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public T getOne(ID id) {
        return findById(id).orElse(null);
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Optional<T> findOne(Specification<T> spec) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return null;
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return null;
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return null;
    }

    @Override
    public long count(Specification<T> spec) {
        return 0;
    }
}
