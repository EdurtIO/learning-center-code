package io.edurt.lc.guice;

import com.google.inject.Provider;

public class GuiceAutoProvider
        implements Provider<GuiceAutoProviderService>
{
    @Override
    public GuiceAutoProviderService get()
    {
        return new GuiceAutoProviderServiceImpl();
    }
}
