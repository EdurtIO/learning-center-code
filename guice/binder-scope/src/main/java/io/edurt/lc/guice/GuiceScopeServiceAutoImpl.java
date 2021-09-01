package io.edurt.lc.guice;

import com.google.inject.Singleton;

@Singleton
public class GuiceScopeServiceAutoImpl
        implements GuiceScopeService
{
    @Override
    public void println(String input)
    {
        System.out.println(input);
    }
}
