package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.session.Session;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;

import javax.persistence.*;
import javax.persistence.metamodel.Metamodel;
import java.util.*;

/**
 * @author Steve Ebersole
 */
public class MybatisTypedQueryImpl<R> extends AbstractMybatisQuery<R> {
    private final String qlString;
	private Session session;
	private Class<R> resultClass;

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
		MappedStatement mappedStatement = new MapperBuilderAssistant(session.getConfiguration(), "")
				.addMappedStatement("id", sqlSource, StatementType.PREPARED, SqlCommandType.SELECT, null, null, null,
						null, null, resultClass, null, false, true, false, null, null, null,
						session.getConfiguration().getDatabaseId(), languageDriver, null);

	}

	/**
	 * 添加 MappedStatement 到 Mybatis 容器
	 */
	protected MappedStatement addMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource,
			SqlCommandType sqlCommandType, Class<?> parameterClass,
			String resultMap, Class<?> resultType, KeyGenerator keyGenerator,
			String keyProperty, String keyColumn) {
//		String statementName = mapperClass.getName() + DOT + id;
		/*if (hasMappedStatement(statementName)) {
			System.err.println(LEFT_BRACE + statementName + "} Has been loaded by XML or SqlProvider, ignoring the injection of the SQL.");
			return null;
		}*/
		/* 缓存逻辑处理 */
		boolean isSelect = false;
		if (sqlCommandType == SqlCommandType.SELECT) {
			isSelect = true;
		}
		return null;
		/*new MapperBuilderAssistant(configuration, resource)
		return builderAssistant
				.addMappedStatement(id, sqlSource, StatementType.PREPARED, sqlCommandType, null, null, null,
						parameterClass, resultMap, resultType, null, !isSelect, isSelect, false, keyGenerator,
						keyProperty, keyColumn, configuration.getDatabaseId(), languageDriver, null);*/
	}

	@Override
	public List<R> getResultList() {
		return null;
	}
}
