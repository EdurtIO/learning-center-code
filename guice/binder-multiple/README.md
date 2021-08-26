# Guice依赖注入(接口多实现)

本文章主要详细讲解Guice依赖注入中的特性接口多实现，一般使用到guice的框架的插件机制都是基于该方式实现。

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-binder-multiple -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-binder-multiple</artifactId>
    <name>Learning Center for Guice Binder(Multiple Service)</name>

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

#### 接口多实现注入

---

如果一个接口有多个实现，如果单单通过`@Inject`和`Module`都难以直接实现，但多实现是经常会出现的，Guice提供了其它注入方式来解决此问题。

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public interface GuiceService
{
    void println(String input);
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuiceJavaService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuiceJavaService
        implements GuiceService
{
    @Override
    public void println(String input)
    {
        System.out.println(String.format("Java %s", input));
    }
}
```

- 在`src/main/java`目录下新建**io.edurt.lc.guice.GuicePythonService**类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

public class GuicePythonService
        implements GuiceService
{
    @Override
    public void println(String input)
    {
        System.out.println(String.format("Python %s", input));
    }
}
```

- 创建`src/main/java`目录下创建**io.edurt.lc.guice.annotation.Java**和**io.edurt.lc.guice.annotation.Python**注解类，用于提供guice框架标识

**io.edurt.lc.guice.annotation.Java**文件内容

```java
package io.edurt.lc.guice.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface Java
{
}
```

**io.edurt.lc.guice.annotation.Python**文件内容

```java
package io.edurt.lc.guice.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface Python
{
}
```

- 接下来在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceMultiple**类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edurt.lc.guice.annotation.Java;
import io.edurt.lc.guice.annotation.Python;

public class TestGuiceMultiple
{
    @Inject
    @Java
    public GuiceService java;

    @Inject
    @Python
    public GuiceService python;

    public static void main(String[] args)
    {
        TestGuiceMultiple application = Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).annotatedWith(Java.class).to(GuiceJavaService.class);
            binder.bind(GuiceService.class).annotatedWith(Python.class).to(GuicePythonService.class);
        }).getInstance(TestGuiceMultiple.class);
        application.java.println("Hello Java Implement");
        application.python.println("Hello Python Implement");
    }
}
```

运行单元测试后,控制台会输出以下信息:

```bash
Java Hello Java Implement
Python Hello Python Implement
```

我们注意看`binder`的配置中，我们将注解与实际的实现类绑定到了一起，这样就实现了绑定多接口实现的功能。

> 注意：在本次程序中我们使用的是lambda表达式进行的代码编程，需要jdk1.8及以上版本

#### 静态代码注入

---

我们如果需要进行静态代码注入服务该怎么写呢？我们参照以前讲解的`Guice依赖注入(构造函数注入)`资源中，在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceMultipleStatic**类进行static的注入，代码如下

```java
package io.edurt.lc.guice;

import com.google.inject.Inject;
import io.edurt.lc.guice.annotation.Java;
import io.edurt.lc.guice.annotation.Python;

public class TestGuiceMultipleStatic
{
    @Inject
    @Java
    public static GuiceService java;

    @Inject
    @Python
    public static GuiceService python;

    public static void main(String[] args)
    {
        com.google.inject.Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).annotatedWith(Java.class).to(GuiceJavaService.class);
            binder.bind(GuiceService.class).annotatedWith(Python.class).to(GuicePythonService.class);
            binder.requestStaticInjection(TestGuiceMultipleStatic.class);
        });
        TestGuiceMultipleStatic.java.println("Hello Java Static Bind");
        TestGuiceMultipleStatic.python.println("Hello Python Static Bind");
    }
}
```

我们只需要在binder阶段将我们的主类注入到guice容器中，也就是我们看到的`binder.requestStaticInjection(TestGuiceMultipleStatic.class);`代码，运行单元测试后,控制台会输出以下信息:

```bash
Java Hello Java Static Bind
Python Hello Python Static Bind
```

#### 使用@Named注解绑定

---

在`src/test/java`目录创建**io.edurt.lc.guice.TestGuiceMultipleNamed**类,文件内容如下

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceMultipleNamed
{
    @Inject
    public GuiceService java;

    @Inject
    public GuiceService python;

    public static void main(String[] args)
    {
        TestGuiceMultipleNamed application = Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).to(GuiceJavaService.class);
            binder.bind(GuiceService.class).to(GuicePythonService.class);
        }).getInstance(TestGuiceMultipleNamed.class);
        application.java.println("Hello Java Named Implement");
        application.python.println("Hello Python Named Implement");
    }
}
```

运行单元测试后,控制台会输出以下信息:

```bash
Exception in thread "main" com.google.inject.CreationException: Unable to create injector, see the following errors:

1) A binding to io.edurt.lc.guice.GuiceService was already configured at io.edurt.lc.guice.TestGuiceMultipleNamed.lambda$main$0(TestGuiceMultipleNamed.java:17).
  at io.edurt.lc.guice.TestGuiceMultipleNamed.lambda$main$0(TestGuiceMultipleNamed.java:18)

1 error
	at com.google.inject.internal.Errors.throwCreationExceptionIfErrorsExist(Errors.java:554)
	at com.google.inject.internal.InternalInjectorCreator.initializeStatically(InternalInjectorCreator.java:161)
	at com.google.inject.internal.InternalInjectorCreator.build(InternalInjectorCreator.java:108)
	at com.google.inject.Guice.createInjector(Guice.java:87)
	at com.google.inject.Guice.createInjector(Guice.java:69)
	at com.google.inject.Guice.createInjector(Guice.java:59)
	at io.edurt.lc.guice.TestGuiceMultipleNamed.main(TestGuiceMultipleNamed.java:16)
```

这是因为我们使用了属性绑定了多接口实现，导致guice无法识别具体是哪个实现类，不过guice是强大的这种问题也被考虑到了，只需要使用`@Named`模板生成注解即可解决，我们将代码修改为以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class TestGuiceMultipleNamed
{
    @Inject
    @Named("Java")
    public GuiceService java;

    @Inject
    @Named("Python")
    public GuiceService python;

    public static void main(String[] args)
    {
        TestGuiceMultipleNamed application = Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).annotatedWith(Names.named("Java")).to(GuiceJavaService.class);
            binder.bind(GuiceService.class).annotatedWith(Names.named("Python")).to(GuicePythonService.class);
        }).getInstance(TestGuiceMultipleNamed.class);
        application.java.println("Hello Java Named Implement");
        application.python.println("Hello Python Named Implement");
    }
}
```

运行单元测试后,控制台会输出以下信息:

```bash
Java Hello Java Named Implement
Python Hello Python Named Implement
```

这个示例也很好理解，其实我们只是做了两步操作

- 在绑定实现的时候使用`annotatedWith(Names.named("Java"))`进行对该服务实现做名称标志
- 在需要使用服务实现的地方使用`@Named("Java")`进行服务的引用即可

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/binder-multiple)
