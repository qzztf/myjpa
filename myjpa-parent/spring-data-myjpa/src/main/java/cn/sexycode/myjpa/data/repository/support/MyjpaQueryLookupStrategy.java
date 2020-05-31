package cn.sexycode.myjpa.data.repository.support;

import cn.sexycode.myjpa.data.repository.query.MyjpaQueryImpl;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;

/**
 * Query lookup strategy to execute finders.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public final class MyjpaQueryLookupStrategy {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private MyjpaQueryLookupStrategy() {}

	/**
	 * Base class for {@link QueryLookupStrategy} implementations that need access to an {@link EntityManager}.
	 *
	 * @author Oliver Gierke
	 * @author Thomas Darimont
	 */
	private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {

		private final EntityManager em;
		private final QueryExtractor provider;

		/**
		 * Creates a new {@link AbstractQueryLookupStrategy}.
		 *
		 * @param em
		 * @param extractor
		 */
		public AbstractQueryLookupStrategy(EntityManager em, QueryExtractor extractor) {

			this.em = em;
			this.provider = extractor;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.repository.query.QueryLookupStrategy#resolveQuery(java.lang.reflect.Method, org.springframework.data.repository.core.RepositoryMetadata, org.springframework.data.projection.ProjectionFactory, org.springframework.data.repository.core.NamedQueries)
		 */
		@Override
		public final RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
				NamedQueries namedQueries) {
			return resolveQuery(new MyjpaQueryMethod(method, metadata, factory, provider), em, namedQueries);
		}

		protected abstract RepositoryQuery resolveQuery(MyjpaQueryMethod method, EntityManager em, NamedQueries namedQueries);
	}


	/**
	 * {@link QueryLookupStrategy} that tries to detect a declared query declared via {@link Query} annotation followed by
	 * a JPA named query lookup.
	 *
	 * @author Oliver Gierke
	 * @author Thomas Darimont
	 */
	private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {

		private final EvaluationContextProvider evaluationContextProvider;

		/**
		 * Creates a new {@link DeclaredQueryLookupStrategy}.
		 *
		 * @param em
		 * @param extractor
		 * @param evaluationContextProvider
		 */
		public DeclaredQueryLookupStrategy(EntityManager em, QueryExtractor extractor,
				EvaluationContextProvider evaluationContextProvider) {

			super(em, extractor);
			this.evaluationContextProvider = evaluationContextProvider;
		}

		@Override
		protected RepositoryQuery resolveQuery(MyjpaQueryMethod method, EntityManager em, NamedQueries namedQueries) {
			return new MyjpaQueryImpl(method, em);
		}
	}


	/**
	 * Creates a {@link QueryLookupStrategy} for the given {@link EntityManager} and {@link Key}.
	 *
	 * @param em must not be {@literal null}.
	 * @param key may be {@literal null}.
	 * @param extractor must not be {@literal null}.
	 * @param evaluationContextProvider must not be {@literal null}.
	 * @return
	 */
	public static QueryLookupStrategy create(EntityManager em, @Nullable Key key, QueryExtractor extractor,
			EvaluationContextProvider evaluationContextProvider) {

		Assert.notNull(em, "EntityManager must not be null!");
		Assert.notNull(extractor, "QueryExtractor must not be null!");
		Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");

		switch (key != null ? key : Key.USE_DECLARED_QUERY) {

			case USE_DECLARED_QUERY:
				return new DeclaredQueryLookupStrategy(em, extractor, evaluationContextProvider);
			case CREATE_IF_NOT_FOUND:

			default:
				return null;
		}
	}
}
