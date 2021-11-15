package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;
import org.aopalliance.intercept.MethodInvocation;

@ImplementedBy(value = GuiceAopSeniorServiceImpl.class)
public interface GuiceAopSeniorService
{
    void before(MethodInvocation invocation);
}
