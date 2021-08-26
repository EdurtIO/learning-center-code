package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class TestGuiceMultipleNamed
{
    @Inject
    @Named("Java")
    public GuiceService java;

    @Inject
    @Named("Python")
    public GuiceService python;

    public static void main(String[] args)
    {
        TestGuiceMultipleNamed application = Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).annotatedWith(Names.named("Java")).to(GuiceJavaService.class);
            binder.bind(GuiceService.class).annotatedWith(Names.named("Python")).to(GuicePythonService.class);
        }).getInstance(TestGuiceMultipleNamed.class);
        application.java.println("Hello Java Named Implement");
        application.python.println("Hello Python Named Implement");
    }
}
