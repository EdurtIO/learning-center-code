package io.edurt.lc.guice;

import com.google.inject.name.Named;

public class PrintlnServiceImpl
        implements PrintlnService
{
    @Override
    @Named(value = "println")
    public void println(String input)
    {
        System.out.println(input);
    }
}
