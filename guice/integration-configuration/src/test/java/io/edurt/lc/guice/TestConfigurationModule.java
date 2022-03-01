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
