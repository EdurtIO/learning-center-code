# Guice AOP(进阶版)

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-aop-senior -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-aop-senior</artifactId>
    <name>Learning Center for Guice AOP(Senior)</name>

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

#### 初始化Service
---

首先我们定义服务Service，这个服务有一个简单的方法`println`.

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopSeniorService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;
import org.aopalliance.intercept.MethodInvocation;

@ImplementedBy(value = GuiceAopSeniorServiceImpl.class)
public interface GuiceAopSeniorService
{
    void before(MethodInvocation invocation);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopSeniorServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import org.aopalliance.intercept.MethodInvocation;

public class GuiceAopSeniorServiceImpl
        implements GuiceAopSeniorService
{
    @Override
    public void before(MethodInvocation invocation)
    {
        System.out.println(String.format("Before method [%s]", invocation.getMethod().getName()));
    }
}
```

#### AOP注入依赖
---

Guice允许在关联AOP之前将AOP的依赖都注入到容器中!

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopInjectionMethodInterceptor**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class GuiceAopInjectionMethodInterceptor
        implements MethodInterceptor
{
    @Inject
    private GuiceAopSeniorService service;

    @Override
    public Object invoke(MethodInvocation invocation)
            throws Throwable
    {
        service.before(invocation);
        Object response;
        try {
            response = invocation.proceed();
        }
        finally {
            System.out.println(String.format("After [%s]", invocation.getMethod().getName()));
        }
        return response;
    }
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.PrintlnService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(value = PrintlnServiceImpl.class)
public interface PrintlnService
{
    void println(String input);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.PrintlnServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.name.Named;

public class PrintlnServiceImpl
        implements PrintlnService
{
    @Override
    @Named(value = "println")
    public void println(String input)
    {
        System.out.println(input);
    }
}
```

> `PrintlnService`和`PrintlnServiceImpl`用于测试新的服务产出.

- 接下来在`src/test/java`目录创建`io.edurt.lc.guice.TestGuiceAopSenior`类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import org.junit.Test;

public class TestGuiceAopSenior
{
    @Inject
    private PrintlnService printlnService;

    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> {
            GuiceAopInjectionMethodInterceptor injectionMethodInterceptor = new GuiceAopInjectionMethodInterceptor();
            binder.requestInjection(injectionMethodInterceptor);
            binder.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Names.named("println")), injectionMethodInterceptor);
        });
        TestGuiceAopSenior application = injector.getInstance(TestGuiceAopSenior.class);
        application.printlnService.println("Hello Guice AOP");
    }
}
```

我们运行程序输出

```bash
Before method [println]
Hello Guice AOP
After [println]
```

> 需要注意的是:
> `binder.requestInjection(injectionMethodInterceptor);` 该段代码用于注入自定义的AOP服务
> `binder.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Names.named("println")), injectionMethodInterceptor);`这里的第二个参数不能使用`Matchers.any()`否则会出现死循环,因为容器会不断的进行aop操作

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/aop-senior)
