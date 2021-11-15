package io.edurt.lc.guice;

import org.aopalliance.intercept.MethodInvocation;

public class GuiceAopSeniorServiceImpl
        implements GuiceAopSeniorService
{
    @Override
    public void before(MethodInvocation invocation)
    {
        System.out.println(String.format("Before method [%s]", invocation.getMethod().getName()));
    }
}
