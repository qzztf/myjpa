package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.mybatis.MyjpaConfiguration;

public interface SessionFactoryBuilder {
    SessionFactory build(MyjpaConfiguration config);
}
