package io.edurt.lc.guice;

import com.google.inject.Provider;

public class GuiceProvider
        implements Provider<GuiceProviderService>
{
    @Override
    public GuiceProviderService get()
    {
        return new GuiceProviderServiceImpl();
    }
}
