package io.edurt.lc.guice;

import com.google.inject.AbstractModule;
import io.edurt.lc.guice.annotation.GuiceCustomScope;

public class GuiceCustomScopeModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        bindScope(GuiceCustomScope.class, new GuiceCustomScopeImpl());
    }
}
