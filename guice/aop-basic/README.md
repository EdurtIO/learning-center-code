# Guice AOP(基础版)

本教程主要详细讲解Guice的一些AOP方式,通过该简单教程让我们可以快速使用Guice进行AOP开发,后续我们会更深入讲解更多Guice中的AOP.

#### 基础环境

---

| 技术  | 版本  |
| ----- | ----- |
| Java  | 1.8+  |
| Guice | 4.2.3 |

#### 初始化项目

---

- 初始化项目

```bash
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-aop-basic -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
```

- 修改pom.xml增加Guice依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <parent>
        <artifactId>lc-guice</artifactId>
        <groupId>io.edurt.lc.guice</groupId>
        <version>1.0.0</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>

    <artifactId>guice-aop-basic</artifactId>
    <name>Learning Center for Guice AOP(Basic)</name>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.google.inject</groupId>
            <artifactId>guice</artifactId>
            <version>4.2.3</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

`guice`: guice就是我们核心要使用的依赖

#### 初始化Service
---

首先我们定义服务Service，这个服务有一个简单的方法`println`.

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public interface GuiceAopService
{
    void println(String input);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceAopServiceImpl
        implements GuiceAopService
{
    @Override
    public void println(String input)
    {
        System.out.println("Guice AOP Service : " + input);
    }
}
```

#### 定义AOP模型

---

接下来定义一个AOP的实现。在Aopalliance中（大家都认可的AOP联盟）实现我们的拦截器。`GuiceAopLoggerMethodInterceptor`拦截器也没有做什么特别的事情，只是记录些执行的时间，由于执行时间比较短我们用纳秒来表示。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopLoggerMethodInterceptor**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class GuiceAopLoggerMethodInterceptor
        implements MethodInterceptor
{
    @Override
    public Object invoke(MethodInvocation methodInvocation)
            throws Throwable
    {
        String methodName = methodInvocation.getMethod().getName();
        long startTime = System.nanoTime();
        System.out.println(String.format("Before Method[%s] start in %s", methodName, startTime));
        Object response;
        try {
            response = methodInvocation.proceed();
        }
        finally {
            long endTime = System.nanoTime();
            System.out.println(String.format("After Method[%s] stop %s, Elapsed(nanosecond): %d", methodName, endTime, (endTime - startTime)));
        }
        return response;
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceAOP**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import org.junit.Test;

public class TestGuiceAOP
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bindInterceptor(Matchers.any(), Matchers.any(), new GuiceAopLoggerMethodInterceptor()));
        GuiceAopService aopService = injector.getInstance(GuiceAopService.class);
        aopService.println("AOP Demo");
    }
}
```

我们运行程序输出

```bash
Before Method[println] start in 206944653651583
Guice AOP Service : AOP Demo
After Method[println] stop 206944657624333, Elapsed(nanosecond): 3972750
```

需要注意的是:

- 由于使用了AOP我们的服务得到的不再是我们写的服务实现类了，而是一个继承的子类，这个子类是在内存中完成的。
- 除了第一次调用比较耗时外(Guice内部做了比较多的处理),其它调用事件毫秒数会降低(这个和我们服务本身有关系)
- 类必须是public或者package(default)
- 类不能是final类型
- 方法必须是public,package或者protected
- 方法不能使final类型
- 实例必须通过Guice的@Inject注入或者有一个无参数的构造函数

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/aop-basic)
