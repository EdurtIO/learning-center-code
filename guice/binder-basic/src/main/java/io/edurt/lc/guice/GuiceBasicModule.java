package io.edurt.lc.guice;

import com.google.inject.AbstractModule;

public class GuiceBasicModule
        extends AbstractModule
{

    @Override
    protected void configure()
    {
        System.out.println("Hello, GuiceBasicModule");
        bind(GuiceBasicService.class).to(GuiceBasicServiceImpl.class);
    }
}
