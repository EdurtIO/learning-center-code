# Guice依赖注入(Provider)

本文章主要详细讲解Guice依赖注入中的Provider服务注入实现，一般都是用于外部服务的注入，比如实现Redis等。

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-binder-provider -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-provider</artifactId>
    <name>Learning Center for Guice Binder(Provider)</name>

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

#### 实现Provider注入

---

如果想要注入一个服务我们可以使用`Provider`进行实现。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceProviderService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public interface GuiceProviderService
{
    void println(String input);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceProviderServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice.io.edurt.lc.guice;

import io.edurt.lc.guice.GuiceProviderService;

public class GuiceProviderServiceImpl
        implements GuiceProviderService
{
    @Override
    public void println(String input)
    {
        System.out.println(input);
    }
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceProvider**类文件用于实现注入,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.Provider;

public class GuiceProvider
        implements Provider<GuiceProviderService>
{
    @Override
    public GuiceProviderService get()
    {
        return new GuiceProviderServiceImpl();
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceProvider**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class TestGuiceProvider
{
    @Inject
    private GuiceProviderService providerService;

    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceProviderService.class).toProvider(GuiceProvider.class));
        TestGuiceProvider application = injector.getInstance(TestGuiceProvider.class);
        application.providerService.println("Hello Guice Provider");
    }
}
```

我们运行程序输出

```bash
Hello Guice Provider
```

我们注意看`binder`的配置中，我们使用的是`toProvider`将实现类绑定到了`Service`接口中，这样就实现了对Provider的注入。

#### 使用`@ProvidedBy`注解绑定

---

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAutoProviderService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.ProvidedBy;

@ProvidedBy(value = GuiceAutoProvider.class)
public interface GuiceAutoProviderService
{
    void println(String input);
}
```

> 注意我们使用`@ProvidedBy`标志了接口的实现类，这样的话我们就可以实现自动注入。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAutoProviderServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceAutoProviderServiceImpl
        implements GuiceAutoProviderService
{
    @Override
    public void println(String input)
    {
        System.out.println(input);
    }
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceAutoProvider**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.Provider;

public class GuiceAutoProvider
        implements Provider<GuiceAutoProviderService>
{
    @Override
    public GuiceAutoProviderService get()
    {
        return new GuiceAutoProviderServiceImpl();
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceAutoProvider**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceAutoProvider
{
    @Inject
    private GuiceAutoProviderService autoProviderService;

    public static void main(String[] args)
    {
        TestGuiceAutoProvider application = Guice.createInjector().getInstance(TestGuiceAutoProvider.class);
        application.autoProviderService.println("Hello Auto Guice Provider");
    }
}
```

我们运行程序输出

```bash
Hello Auto Guice Provider
```

在代码中我们可以看到我们没有去绑定Module也可以实现注入。

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/binder-provider)

