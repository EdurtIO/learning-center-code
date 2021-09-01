# Guice依赖注入(Scope)

本文章主要详细讲解Guice依赖注入中的一些高级选项，他们分别是`Scope`，`Eagerly Loading Bindings`，`Stage`，`Optional Injection`。我们将一一对他们进行讲解。

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-binder-scope -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-scope</artifactId>
    <name>Learning Center for Guice Binder(Scope)</name>

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

#### Singleton

---

Guice支持我们在其他DI框架中逐渐习惯的`Scope`和`Scope`机制。Guice默认提供已定义依赖项的新实例。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceScopeService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public interface GuiceScopeService
{
    void println(String input);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceScopeServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceScopeServiceImpl
        implements GuiceScopeService
{
    @Override
    public void println(String input)
    {
        System.out.println(input);
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceScope**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import org.junit.Test;

public class TestGuiceScope
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceScopeService.class).to(GuiceScopeServiceImpl.class).in(Scopes.SINGLETON));
        GuiceScopeService service = injector.getInstance(GuiceScopeService.class);
        service.println("Singleton Scope");
    }
}
```

我们运行程序输出

```bash
Singleton Scope
```

> 通过代码`binder.bind(GuiceScopeService.class).to(GuiceScopeServiceImpl.class).in(Scopes.SINGLETON)`我们指定了GuiceScopeService的Scope，他将会被标志为一个单例实例。当然我们也可以使用`@Singleton`标志该类的作用域

- 在`src/test/java`目录创建**GuiceScopeServiceAutoImpl**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Singleton;

@Singleton
public class GuiceScopeServiceAutoImpl
        implements GuiceScopeService
{
    @Override
    public void println(String input)
    {
        System.out.println(input);
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceScopeAuto**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceScopeAuto
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceScopeService.class).to(GuiceScopeServiceAutoImpl.class));
        GuiceScopeService service = injector.getInstance(GuiceScopeService.class);
        service.println("Singleton Auto Scope");
    }
}
```

我们运行程序输出

```bash
Singleton Auto Scope
```

两种方式实现的效果都是一致的。此时`GuiceScopeService`会被构建为单例实例。

当然还有一个`asEagerSingleton()`方法也可以用来标记单例模式。

他们的对比图如下：

|使用方式|PRODUCTION|DEVELOPMENT|
|---|---|---|
|.asEagerSingleton()|eager|eager|
|.in(Singleton.class)|eager|lazy|
|.in(Scopes.SINGLETON)|eager|lazy|
|@Singleton|eager*|lazy|

#### 自定义Scope

---

Guice还支持我们用户自定义作用域，通常情况下我们不需要自己实现Scope，一般内置的作用域对于大多数的应用已经足够了。如果您正在编写一个web应用程序，那么`ServletModule`为HTTP请求和HTTP会话提供了简单的、良好作用域实现是一个很好的想法。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.annotation.GuiceCustomScope**类文件,实现自定义Scope注解,在文件输入以下内容

> Scope注解用于标记当前Scope在容器中使用的作用域。将使用它来注释guice构造的类型，`@Provides`方法和bind语法中的`in()`

```java
package io.edurt.lc.guice.annotation;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, METHOD})
@Retention(RUNTIME)
@ScopeAnnotation
public @interface GuiceCustomScope
{
}
```

在使用自定义Scope时，请确保导入了正确的Scope注解。否则，您可能会得到一个`SCOPE_NOT_FOUND`错误。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceCustomScopeImpl**类文件, 在文件输入以下内容

> Scope接口确保每个Scope实例拥有一到多个类型实例。

```java
package io.edurt.lc.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class GuiceCustomScopeImpl
        implements Scope
{
    ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped)
    {
        return () -> {
            T instance = (T) threadLocal.get();
            if (instance == null) {
                instance = unscoped.get();
                threadLocal.set(instance);
            }
            return instance;
        };
    }
}
```

我们在上述代码中实现了一个简单线程抽取Scope，我们只是为了做测试使用，具体的Scope还需要根据业务自己使用。当我们传递的线程中没有构造一个对象时，先构造一个，然后放入线程上下文中，以后每次都从线程中获取对象。

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceCustomScope**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceCustomScope
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(GuiceScopeService.class).to(GuiceScopeServiceImpl.class).in(new GuiceCustomScopeImpl());
        });
        for (int i = 0; i < 3; i++) {
            System.out.println(injector.getInstance(GuiceScopeService.class).hashCode());
        }
    }
}
```

运行程序后我们得到以下结果：

```bash
508198356
508198356
508198356
```

我们通过结果得到运行了3次后的实例hashCode是一致的，这就说明我们的自定义Scope已经起作用了。如果新的实例构建后那么hashCode将会被改变。

- 接下来在`src/main/java`目录创建**io.edurt.lc.guice.GuiceCustomScopeModule**类文件绑定自定义Scope注解，我们通过实现Module进行注入,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.AbstractModule;
import io.edurt.lc.guice.annotation.GuiceCustomScope;

public class GuiceCustomScopeModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        bindScope(GuiceCustomScope.class, new GuiceCustomScopeImpl());
    }
}
```

需要使用到改Module只需要在Guice.createInjector构建的时候添加该Module即可，代码如下：

```java
Injector injector = Guice.createInjector(new GuiceCustomScopeModule(), binder -> {
    binder.bind(GuiceScopeService.class).to(GuiceScopeServiceImpl.class).in(new GuiceCustomScopeImpl());
});
```

在`ScopeService`类上使用`@GuiceCustomScope`注解即可。

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/binder-scope)
