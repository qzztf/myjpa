package cn.sexycode.mybatis.jpa.plus.injector;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 注入自定义sql
 *
 * @author qinzaizhen
 */
public class SqlInjector extends DefaultSqlInjector {

    @Override
    public List<com.baomidou.mybatisplus.core.injector.AbstractMethod> getMethodList() {
        List<com.baomidou.mybatisplus.core.injector.AbstractMethod> list = new ArrayList<>();

        return Stream.of(
                new Insert(),
                new Delete(),
                new DeleteByMap(),
                new DeleteById(),
                new DeleteBatchByIds(),
                new Update(),
                new UpdateById(),
                new SelectById(),
                new SelectBatchByIds(),
                new SelectByMap(),
                new SelectOne(),
                new SelectCount(),
                new SelectMaps(),
                new SelectMapsPage(),
                new SelectObjs(),
                new SelectList(),
                new SelectPage()
        ).collect(toList());
    }

}
