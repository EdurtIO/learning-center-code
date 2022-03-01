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
