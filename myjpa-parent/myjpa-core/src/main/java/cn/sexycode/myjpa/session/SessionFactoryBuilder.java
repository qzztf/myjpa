package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.MyjpaConfiguration;

public interface SessionFactoryBuilder {
    SessionFactory build(MyjpaConfiguration config);
}
