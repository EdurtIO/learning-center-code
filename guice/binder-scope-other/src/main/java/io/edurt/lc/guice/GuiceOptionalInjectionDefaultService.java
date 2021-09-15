package io.edurt.lc.guice;

public class GuiceOptionalInjectionDefaultService
        implements GuiceService
{
    @Override
    public void println()
    {
        System.out.println("Optional Injection Default");
    }
}
