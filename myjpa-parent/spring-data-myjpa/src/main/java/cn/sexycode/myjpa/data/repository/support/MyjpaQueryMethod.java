package cn.sexycode.myjpa.data.repository.support;

import cn.sexycode.util.core.str.StringUtils;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;

import java.lang.reflect.Method;

/**
 * JPA specific extension of {@link QueryMethod}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Nicolas Cirigliano
 * @author Mark Paluch
 * @author Сергей Цыпанов
 */
public class MyjpaQueryMethod extends JpaQueryMethod {

    private final Method method;

    private final RepositoryMetadata metadata;

    /**
     * Creates a {@link MyjpaQueryMethod}.
     *
     * @param method    must not be {@literal null}
     * @param metadata  must not be {@literal null}
     * @param factory   must not be {@literal null}
     * @param extractor must not be {@literal null}
     */
    public MyjpaQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
            QueryExtractor extractor) {

        super(method, metadata, factory, extractor);
        this.method = method;
        this.metadata = metadata;
    }

    @Override
    public String getNamedQueryName() {
        //返回mybatis statement id
        return StringUtils.join(".", new String[]{metadata.getRepositoryInterface().getName(), method.getName()});
    }
}
