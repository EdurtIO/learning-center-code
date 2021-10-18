package io.edurt.lc.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class GuiceAopLoggerMethodInterceptor
        implements MethodInterceptor
{
    @Override
    public Object invoke(MethodInvocation methodInvocation)
            throws Throwable
    {
        String methodName = methodInvocation.getMethod().getName();
        long startTime = System.nanoTime();
        System.out.println(String.format("Before Method[%s] start in %s", methodName, startTime));
        Object response;
        try {
            response = methodInvocation.proceed();
        }
        finally {
            long endTime = System.nanoTime();
            System.out.println(String.format("After Method[%s] stop %s, Elapsed(nanosecond): %d", methodName, endTime, (endTime - startTime)));
        }
        return response;
    }
}
