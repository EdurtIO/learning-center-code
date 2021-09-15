package io.edurt.lc.guice;

public class GuiceEagerlyLoadingBindingsService
{
    public GuiceEagerlyLoadingBindingsService()
    {
        System.out.println("Init Success");
    }

    public void println()
    {
        System.out.println("After Eagerly Loading Bindings");
    }
}
