# Guice AOP(Matcher)

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-aop-matcher -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-aop-matcher</artifactId>
    <name>Learning Center for Guice AOP(Matcher)</name>

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

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopMatcherService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(value = GuiceAopMatcherServiceImpl.class)
public interface GuiceAopMatcherService
{
    void println(String input);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopMatcherServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceAopMatcherServiceImpl
        implements GuiceAopMatcherService
{
    @Override
    public void println(String input)
    {
        System.out.println("Matcher input : " + input);
    }
}
```

#### AOP注入依赖

---

Guice允许在关联AOP之前将AOP的依赖都注入到容器中!

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAopMatcherMethodInterceptor**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class GuiceAopMatcherMethodInterceptor
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

- 在`src/test/java`目录创建`io.edurt.lc.guice.GuiceAopJavaServiceMatcher`类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.matcher.Matcher;

public class GuiceAopJavaServiceMatcher
        implements Matcher<Class<?>>
{
    @Override
    public boolean matches(Class<?> aClass)
    {
//        return aClass == GuiceAopMatcherService.class;
        return aClass == GuiceAopMatcherServiceImpl.class;
    }

    @Override
    public Matcher<Class<?>> and(Matcher<? super Class<?>> matcher)
    {
        return null;
    }

    @Override
    public Matcher<Class<?>> or(Matcher<? super Class<?>> matcher)
    {
        return null;
    }
}
```

- 接下来在`src/test/java`目录创建`io.edurt.lc.guice.TestGuiceAopJavaServiceMatcher`类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import org.junit.Test;

public class TestGuiceAopJavaServiceMatcher
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bindInterceptor(new GuiceAopJavaServiceMatcher(),
                Matchers.any(),
                new GuiceAopMatcherMethodInterceptor()));
        GuiceAopMatcherService javaServiceMatcher = injector.getInstance(GuiceAopMatcherServiceImpl.class);
        javaServiceMatcher.println("Hello Guice!!!");
    }
}
```

我们运行程序输出

```bash
Before Method[println] start in 174453945750833
Matcher input : Hello Guice!!!
After Method[println] stop 174453952765375, Elapsed(nanosecond): 7014542
```

> 需要注意的是:
> `return aClass == GuiceAopMatcherService.class;` 这里判定的是相对的类,在我们的示例中是`return aClass == GuiceAopMatcherServiceImpl.class;`
> 如果要使用父类自动转换的话需要自己解析类的实现即可

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/aop-matcher)
