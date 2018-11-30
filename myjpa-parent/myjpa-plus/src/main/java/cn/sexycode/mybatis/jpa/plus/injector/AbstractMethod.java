package cn.sexycode.mybatis.jpa.plus.injector;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * <p>
 * 抽象的注入方法类
 * </p>
 *
 * @author hubin
 * @since 2018-04-06
 */
public abstract class AbstractMethod extends com.baomidou.mybatisplus.core.injector.AbstractMethod implements Constants {

    /**
     * 注入自定义方法
     */
    @Override
    public void inject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        this.configuration = builderAssistant.getConfiguration();
        this.builderAssistant = builderAssistant;
        this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
        Class<?> modelClass = extractModelClass(mapperClass);
        if (null != modelClass) {
            /**
             * 注入自定义方法
             */
            TableInfo tableInfo = MyTableInfoHelper.initTableInfo(builderAssistant, modelClass);
            injectMappedStatement(mapperClass, modelClass, tableInfo);
        }
    }


}
