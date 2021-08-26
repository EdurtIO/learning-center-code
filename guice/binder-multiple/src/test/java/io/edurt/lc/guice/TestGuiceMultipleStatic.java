package io.edurt.lc.guice;

import com.google.inject.Inject;
import io.edurt.lc.guice.annotation.Java;
import io.edurt.lc.guice.annotation.Python;

public class TestGuiceMultipleStatic
{
    @Inject
    @Java
    public static GuiceService java;

    @Inject
    @Python
    public static GuiceService python;

    public static void main(String[] args)
    {
        com.google.inject.Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).annotatedWith(Java.class).to(GuiceJavaService.class);
            binder.bind(GuiceService.class).annotatedWith(Python.class).to(GuicePythonService.class);
            binder.requestStaticInjection(TestGuiceMultipleStatic.class);
        });
        TestGuiceMultipleStatic.java.println("Hello Java Static Bind");
        TestGuiceMultipleStatic.python.println("Hello Python Static Bind");
    }
}
