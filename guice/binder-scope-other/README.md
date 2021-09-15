# Guice依赖注入(Scope其他方式)

本文章主要详细讲解Guice依赖注入中的一些高级选项，他们分别是`Eagerly Loading Bindings`，`Stage`，`Optional Injection`。我们将一一对他们进行讲解。

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-binder-scope-other -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-scope-other</artifactId>
    <name>Learning Center for Guice Binder(Scope Other)</name>

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

#### Eagerly Loading Bindings

---

除绑定Scope外，对象默认在第一次调用时被创建，也就是`Eagerly Loading Bindings`延时加载，Guice也允许对象在注入到Guice容器中时就被创建出来(只是针对单例模式才有效)。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceEagerlyLoadingBindingsService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceEagerlyLoadingBindingsService
{
    public GuiceEagerlyLoadingBindingsService()
    {
        System.out.println("Init Success");
    }

    public void println()
    {
        System.out.println("After Eagerly Loading Bindings");
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceEagerlyLoadingBindings**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceEagerlyLoadingBindings
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceEagerlyLoadingBindingsService.class).asEagerSingleton());
        injector.getInstance(GuiceEagerlyLoadingBindingsService.class).println();
    }
}
```

我们运行程序输出

```bash
Init Success
After Eagerly Loading Bindings
```

通过运行结果我们可以看到在`getInstance`获取实例时,该实例就被构建出来.

#### Stages

---

Guice中有默认三种运行方式来控制Guice容器的加载速度.在`com.google.inject.Stage`枚举中提供了`TOOL`，`DEVELOPMENT`，`PRODUCTION`三种模式。

默认源码中使用的是`DEVELOPMENT`方式,源码如下

```java
public static Injector createInjector(Iterable<?extends Module> modules){
        return createInjector(Stage.DEVELOPMENT,modules);
        }
```

如需指定自己的格式,代码如下

```java
Injector injector=Guice.createInjector(Stage.DEVELOPMENT,binder->binder.bind(GuiceEagerlyLoadingBindingsService.class).asEagerSingleton());
```

在构建时指定`Stage`即可.

#### Optional Injection

---

`Optional Injection`可以用于我们选择性的注入内容, 也就是添加一个默认对象.

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public interface GuiceService
{
    void println();
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceOptionalInjectionDefaultService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceOptionalInjectionDefaultService
        implements GuiceService
{
    @Override
    public void println()
    {
        System.out.println("Optional Injection Default");
    }
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceOptionalInjectionDefaultService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceOptionalInjectionCustomService
        implements GuiceService
{
    @Override
    public void println()
    {
        System.out.println("Optional Injection Custom");
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceOptionalInjection**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceOptionalInjection
{
    @Inject(optional = true)
    private GuiceService service = new GuiceOptionalInjectionDefaultService();

    @Test
    public void testDefault()
    {
        Injector injector = Guice.createInjector(binder -> {
        });
        injector.getInstance(TestGuiceOptionalInjection.class).service.println();
    }

    @Test
    public void testCustom()
    {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).to(GuiceOptionalInjectionCustomService.class);
        });
        injector.getInstance(TestGuiceOptionalInjection.class).service.println();
    }
}
```

当我们运行`testDefault()`时,控制台输出

```bash
Optional Injection Default
```

当我们运行`testCustom()`时,控制台输出

```bash
Optional Injection Custom
```

Guice容器中无法获取获取一个`GuiceService`服务那么就使用默认的`GuiceOptionalInjectionDefaultService`，否则就是用获取到的`GuiceOptionalInjectionCustomService`服务。

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/binder-scope-other)
