# Guice依赖注入(构造函数)

本教程主要详细讲解Guice的构造函数注入. 我们将通过详细的代码以及步骤进行讲解.

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-binder-constructor -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-constructor</artifactId>
    <name>Learning Center for Guice Binder(Constructor)</name>

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

#### 构造函数注入

---

在Guice中我们可以通过将需要的实体信息通过构造函数直接注入到我们需要的任意地方，我们通过列举一个例子来实际说明。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceConstructorService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(GuiceConstructorServiceImpl.class)
public interface GuiceConstructorService
{
    void print(String source);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceConstructorServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceConstructorServiceImpl
        implements GuiceConstructorService
{
    @Override
    public void print(String source)
    {
        System.out.println(String.format("Hello Guice Binder For Constructor, %s", source));
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceConstructor**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceConstructor
{
    private GuiceConstructorService service;

    @Inject
    public TestGuiceConstructor(GuiceConstructorService service)
    {
        this.service = service;
    }

    public GuiceConstructorService getService()
    {
        return service;
    }

    public static void main(String[] args)
    {
        TestGuiceConstructor test = Guice.createInjector().getInstance(TestGuiceConstructor.class);
        test.getService().print("Test Case 1");
    }
}
```

运行单元测试后,控制台会输出以下信息:

```bash
Hello Guice Binder For Constructor, Test Case 1
```

这个示例很好理解，实际就是说我们将`GuiceConstructorService`接口通过`@Inject`注入到了`TestGuiceConstructor`应用中。当然我们通过`@ImplementedBy(GuiceConstructorServiceImpl.class)`实现了类似`GuiceConstructorService service = new GuiceConstructorServiceImpl()`的操作，不过每次会生成一个新的实例，如果需要单例模式的话，需要单独操作。

> 注意：在本次程序中我们并没有通过Module关联到Guice，方便我们快速测试应用等。
> 我们无法通过非Guice容器进行注入，以下就是一个错误的示例
> static也是无法进行注入的

```java
package io.edurt.lc.guice;

import com.google.inject.Inject;

public class TestGuiceConstructorNo
{
    @Inject
    private GuiceConstructorService service;

    public GuiceConstructorService getService()
    {
        return service;
    }

    public static void main(String[] args)
    {
        TestGuiceConstructorNo test = new TestGuiceConstructorNo();
        test.getService().print("Test Case 1");
    }
}
```

我们运行上述代码，会提示以下错误信息

```bash
Exception in thread "main" java.lang.NullPointerException
	at io.edurt.lc.guice.TestGuiceConstructorNo.main(TestGuiceConstructorNo.java:18)
```

这也就说明我们无法在非Guice容器中进行实例注入

#### 多参数注入

---

上述实例我们只是注入了一个参数，那我们尝试一下多参数注入。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceConstructorTwoService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(GuiceConstructorTwoServiceImpl.class)
public interface GuiceConstructorTwoService
{
    void print();
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceConstructorTwoServiceImpl**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceConstructorTwoServiceImpl
        implements GuiceConstructorTwoService
{
    @Override
    public void print()
    {
        System.out.println(String.format("Hello Guice Binder For Constructor Two"));
    }
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceConstructorMultiple**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceConstructorMultiple
{
    private GuiceConstructorService service;
    private GuiceConstructorTwoService twoService;

    public GuiceConstructorService getService()
    {
        return service;
    }

    public void setService(GuiceConstructorService service)
    {
        this.service = service;
    }

    public GuiceConstructorTwoService getTwoService()
    {
        return twoService;
    }

    public void setTwoService(GuiceConstructorTwoService twoService)
    {
        this.twoService = twoService;
    }

    @Inject
    public TestGuiceConstructorMultiple(GuiceConstructorService service, GuiceConstructorTwoService twoService)
    {
        this.service = service;
        this.twoService = twoService;
    }

    public static void main(String[] args)
    {
        TestGuiceConstructorMultiple multiple = Guice.createInjector().getInstance(TestGuiceConstructorMultiple.class);
        multiple.getService().print("One");
        multiple.getTwoService().print();
    }
}
```

运行程序后，输出以下结果

```bash
Hello Guice Binder For Constructor, One
Hello Guice Binder For Constructor Two
```

> 我们使用一个`@Inject`也能实现多个参数的实例注入，当然还支持Set方式注入，只需要在参数的set方法上增加`@Inject`注解即可实现，这里我们不多做叙述，可自行实验。

#### static静态参数注入

---

我们说过无法通过static属性直接进行注入使用，方法总是很多的，Guice提供了以下static注入方式.

在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceStatic**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceStatic
{
    @Inject
    private static GuiceConstructorService service;

    public static void main(String[] args)
    {
        Guice.createInjector(binder -> binder.requestStaticInjection(TestGuiceStatic.class));
        TestGuiceStatic.service.print("Static");
    }
}
```

运行程序后，输出以下结果

```bash
Hello Guice Binder For Constructor, Static
```

在代码中我们没有向以上两个示例直接使用`Guice`获取实例，而是使用了`binder.requestStaticInjection`方式进行了注入，这个是和static属性息息相关的，当我们注入static属性的时候要告知Guice我们具体使用static属性的父类，这样Guice才可以帮我们注入进来。

细心的话会想到我们既然使用`binder.requestStaticInjection`方式注入static属性，那么非static属性是不是也可以通过类似的方式注入？

答案是可以的，非static的属性我们需要通过`binder.requestInjection(Type);`方式注入，实例如下：

在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceNonStatic**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceNonStatic
{
    @Inject
    private GuiceConstructorService service;

    public static void main(String[] args)
    {
        TestGuiceNonStatic applicationBinder = new TestGuiceNonStatic();
        Guice.createInjector(binder -> binder.requestInjection(applicationBinder));
        applicationBinder.service.print("Non Static");
    }
}
```

运行程序后，输出以下结果

```bash
Hello Guice Binder For Constructor, Non Static
```

当然我们还可以通过`Guice.createInjector().injectMembers(new Object());`方式注入。

> 注意我们需要创建一个主类的实例才可以注入，使用TestGuiceNonStatic.class是无法注入的

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/binder-constructor)
