# Guice依赖注入(基础版)

本教程主要详细讲解Guice的一些基本注入方式,通过该简单教程让我们可以快速使用Guice进行简单系统化开发,后续我们会更深入讲解更多模块,如果还不了解Guice大家可以先去网上自行了解一下.

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-binder-basic -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-basic</artifactId>
    <name>Learning Center for Guice Binder(Basic)</name>

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
    </dependencies>

</project>
```

`guice`: guice就是我们核心要使用的依赖

#### Guice的绑定模型

---

- 修改`pom.xml`配置文件,在`dependencies`节点中添加以下内容

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice**目录并在该目录下新建`GuiceBasicModule`类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.AbstractModule;

public class GuiceBasicModule
        extends AbstractModule
{

    @Override
    protected void configure()
    {
        System.out.println("Hello, GuiceBasicModule");
    }
}
```

Guice中的绑定模型和Spring中的一样简单,我们通过绑定可以提供给程序任意注入类.

绑定我们需要的Module只需要继承Guice中的`com.google.inject.AbstractModule`即可,在`configure`方法中实现我们需要的绑定信息.

- 在`src/test/java`源代码目录下构建**io.edurt.lc.guice.TestGuiceBasicModule**单元测试类文件用于我们测试代码,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.AbstractModule;

public class GuiceBasicModule
        extends AbstractModule
{

    @Override
    protected void configure()
    {
        System.out.println("Hello, GuiceBasicModule");
    }
}
```

运行单元测试后,控制台会输出以下信息:

```bash
Hello, GuiceBasicModule
```

或者使用maven命令`mvn clean package`,它将输出类似以下内容

```bash
......

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running io.edurt.lc.guice.TestGuiceBasicModule
Hello, GuiceBasicModule
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.094 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

......
```

#### 自定义Class

---

- 在`src/main/java`目录下新建`io.edurt.lc.guice.GuiceBasicService`类文件内容如下

```java
package io.edurt.lc.guice;

public interface GuiceBasicService
{
    void print(String input);
}
```

我们定义了一个接口文件,它构建了一个`print`方法用于信息输出.

- 在`src/main/java`目录下新建`io.edurt.lc.guice.GuiceBasicServiceImpl`类文件内容如下

```java
package io.edurt.lc.guice;

public class GuiceBasicServiceImpl
        implements GuiceBasicService
{
    @Override
    public void print(String input)
    {
        System.out.println(String.format("print %s", input));
    }
}
```

它实现了`io.edurt.lc.guice.GuiceBasicService`类中的打印数据功能

- 修改`src/main/java`目录下`io.edurt.lc.guice.GuiceBasicModule`文件在`configure()`方法中添加以下代码

```java
bind(GuiceBasicService.class).to(GuiceBasicServiceImpl.class);
```

这样我们就很快的绑定了一个服务,类似于Spring中的`@Bean`方式

`bind`标志我们需要绑定的类,`to`标志我们绑定的实现类

- 接下来修改`src/test/java`目录下的`io.edurt.lc.guice.TestGuiceBasicModule`类文件进行定义的服务进行测试,添加以下代码

```java
@Test
public void test_service()
{
    Injector injector = Guice.createInjector(new GuiceBasicModule());
    GuiceBasicService service = injector.getInstance(GuiceBasicService.class);
    service.print("Hello Guice");
}
```

运行单元测试后,控制台会输出以下信息:

```bash
Hello, GuiceBasicModule
print Hello Guice
```

#### 使用`@ImplementedBy`注解

---

使用`@ImplementedBy`很简单,我们只需要在`interface`的接口类上添加`@ImplementedBy(GuiceBasicServiceImpl.class)`注解即可,修改后的代码如下

`@ImplementedBy`告知程序我们的接口具体实现类,Guice会帮我们做自动实例化

- `GuiceBasicService`

```java
package io.edurt.lc.guice;

@ImplementedBy(GuiceBasicServiceImpl.class)
public interface GuiceBasicService
{
    void print(String input);
}
```

`GuiceBasicServiceImpl`类中的内容不变

- 删除`src/test/java`目录下的`io.edurt.lc.guice.TestGuiceBasicModule`类文件中的`GuiceBasicService service = injector.getInstance(GuiceBasicService.class);`代码段即可.

运行单元测试后,控制台会输出以下信息:

```bash
Hello, GuiceBasicModule
print Hello Guice
```

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/binder-basic)
