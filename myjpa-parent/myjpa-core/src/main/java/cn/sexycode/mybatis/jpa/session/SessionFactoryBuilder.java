package cn.sexycode.mybatis.jpa.session;

import cn.sexycode.mybatis.jpa.MyBatisConfiguration;

public interface SessionFactoryBuilder {
    SessionFactory build(MyBatisConfiguration config);
}
