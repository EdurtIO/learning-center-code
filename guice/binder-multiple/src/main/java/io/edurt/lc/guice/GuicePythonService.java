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
