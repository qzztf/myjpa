# myjpa
使用mybatis来实现jpa部分功能，达到jpa统一mybatis的目的。

## 技术原理
大致原理提供中间层，调用jpa方法时，转换成mybatis方法的调用。

## 示例
使用示例可以参看 `myjpa-samples`模块

## 声明
本项目的许多代码来自spring, hibernate，mybatis-plus，tkmapper等优秀项目，你还可以从中看到他们的影子。
在此由衷感谢这些项目提供思路。

最后，文档目前在完善中。
[文档地址](http://myjpa.cn)

roadmap:
-[x] 解析jpa注解（Entity）
-[x] 从SessionFactory中获取到Entity, 构建metamodel
-[x] 与spring，spring data jpa 集成
-[ ] 单表 criteria 查询构造

