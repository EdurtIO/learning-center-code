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
