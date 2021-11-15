package io.edurt.lc.guice;

import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class GuiceAopInjectionMethodInterceptor
        implements MethodInterceptor
{
    @Inject
    private GuiceAopSeniorService service;

    @Override
    public Object invoke(MethodInvocation invocation)
            throws Throwable
    {
        service.before(invocation);
        Object response;
        try {
            response = invocation.proceed();
        }
        finally {
            System.out.println(String.format("After [%s]", invocation.getMethod().getName()));
        }
        return response;
    }
}
