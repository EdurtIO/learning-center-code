# Guice Integration(Properties配置)

在大多数项目中我们经常会使用到读取配置文件,用于适配自定义的属性值等,本教程我们主要通过实现对Properties的解析实现基于Guice的配置解析Module.

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
mvn archetype:generate -DgroupId=io.edurt.lc.guice -DartifactId=guice-integration-configuration -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=1.0.0 -DinteractiveMode=false
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

    <artifactId>guice-integration-configuration</artifactId>
    <name>Learning Center for Guice Integration(Configuration)</name>

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

#### 构建PropertiesUtils

---

`PropertiesUtils`主要用于我们对Properties类型文件的解析.

- 在`src/main/java`目录下新建 **io.edurt.lc.guice.PropertiesUtils** 类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils
{
    private PropertiesUtils()
    {}

    public static Properties loadProperties(String... resourcesPaths)
    {
        Properties props = new Properties();
        for (String location : resourcesPaths) {
            File propertiesFile = new File(location);
            try (InputStream inputStream = new FileInputStream(propertiesFile)) {
                props.load(inputStream);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return props;
    }

    public static Integer getIntValue(Properties properties, String key, Integer defaultValue)
    {
        return Integer.valueOf(getStringValue(properties, key, String.valueOf(defaultValue)));
    }

    public static String getStringValue(Properties properties, String key, String defaultValue)
    {
        if (properties == null) {
            return defaultValue;
        }
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return String.valueOf(properties.getOrDefault(key, defaultValue));
    }

    public static Boolean getBoolValue(Properties properties, String key, Boolean defaultValue)
    {
        return Boolean.valueOf(getStringValue(properties, key, String.valueOf(defaultValue)));
    }
}
```

!!! note

    我们在工具类中提供了几个便捷的方法: 

    - `getIntValue` 获取Integer类型数据   

    - `getStringValue` 获取字符串类型数据

    - `getBoolValue` 获取Boolean类型数据

- 在`src/main/java`目录下新建 **io.edurt.lc.guice.ConfigurationModule** 类文件,在文件输入以下内容

```java
package io.edurt.lc.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.util.Properties;

public class ConfigurationModule
        extends AbstractModule
{
    private Properties bootstrapConfiguration;

    public ConfigurationModule(Properties bootstrapConfiguration)
    {
        this.bootstrapConfiguration = bootstrapConfiguration;
    }

    public ConfigurationModule(String configurationFilePath)
    {
        if (configurationFilePath != null) {
            this.bootstrapConfiguration = PropertiesUtils.loadProperties(configurationFilePath);
        }
    }

    @Override
    protected void configure()
    {
        Names.bindProperties(binder(), bootstrapConfiguration);
    }
}
```

#### 测试示例

---

- 接下来在`src/test/java`目录创建 **io.edurt.lc.guice.TestConfigurationModule** 类文件进行定义的服务进行测试,添加以下代码

```java
package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestConfigurationModule
{
    private String path;
    private Injector injector;

    @Before
    public void before()
    {
        path = this.getClass().getResource("/file.properties").getPath();
        injector = Guice.createInjector(new ConfigurationModule(path));
    }

    @After
    public void after()
    {
    }

    /**
     * Method: configure()
     */
    @Test
    public void testConfigure()
    {
        Assert.assertTrue(injector.getBindings().toString().contains("Test1"));
    }
}
```

- 在`test/resources`目录下新建 **file.properties** 文件,内容如下:

```properties
name=Test1
```

我们运行程序输出

```bash
Connected to the target VM, address: '127.0.0.1:63962', transport: 'socket'
Disconnected from the target VM, address: '127.0.0.1:63962', transport: 'socket'

```

由于我们使用了断言来操作,所以正常运行后系统不会出现任何输入,如果有错误信息,那么控制台会抛出错误信息.

#### 源码地址

---

[GitHub](https://github.com/EdurtIO/learning-center-code/tree/master/guice/integration-configuration)
