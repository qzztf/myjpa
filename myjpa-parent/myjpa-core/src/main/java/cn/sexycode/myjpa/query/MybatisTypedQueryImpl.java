package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.session.Session;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;

import javax.persistence.Parameter;
import javax.persistence.metamodel.Metamodel;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Steve Ebersole
 */
public class MybatisTypedQueryImpl<R> extends AbstractMybatisQuery<R> {
    private final String qlString;
	private Session session;
	private Class<R> resultClass;

	private String queryId;
	//    private MappedStatement mappedStatement;

    private List<Parameter<?>> parameters = new LinkedList<>();
	public MybatisTypedQueryImpl(
			Session session, String qlString) {
        this(session, qlString, null);
	}

	public MybatisTypedQueryImpl(Session session, String qlString, Class<R> resultClass) {
        this.qlString = qlString;
		this.resultClass = resultClass;
		this.session = session;
        Metamodel metamodel = session.getEntityManagerFactory().getMetamodel();
        //		EntityType<R> entity = metamodel.entity(resultClass);
		LanguageDriver languageDriver = session.getConfiguration()
				.getLanguageRegistry().getDriver(RawLanguageDriver.class);
		SqlSource sqlSource = languageDriver.createSqlSource(session.getConfiguration(), qlString, null);
		queryId = "" + qlString.hashCode();
		MappedStatement mappedStatement = new MapperBuilderAssistant(session.getConfiguration(), "criteria:"+ (resultClass == null ? "" : resultClass.getName()))
				.addMappedStatement(queryId, sqlSource, StatementType.STATEMENT, SqlCommandType.SELECT, null, null, null,
						null, null, resultClass, null, false, true, false, null, null, null,
						session.getConfiguration().getDatabaseId(), languageDriver, null);

	}

	@Override
	public List<R> getResultList() {
		List<R> list = session.selectList(queryId);
		Object collection = session.getConfiguration().getObjectFactory().create(List.class);
		MetaObject metaObject = session.getConfiguration().newMetaObject(collection);
		metaObject.addAll(list);
		return (List<R>) collection;
	}

	@Override
	public R getSingleResult() {
		return session.selectOne(queryId);
	}
}
