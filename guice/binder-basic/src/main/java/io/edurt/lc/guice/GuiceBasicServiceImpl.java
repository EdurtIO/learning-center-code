package io.edurt.lc.guice;

public class GuiceBasicServiceImpl
        implements GuiceBasicService
{
    @Override
    public void print(String input)
    {
        System.out.println(String.format("print %s", input));
    }
}
