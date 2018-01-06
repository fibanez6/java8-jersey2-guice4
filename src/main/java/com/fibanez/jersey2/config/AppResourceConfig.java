package com.fibanez.jersey2.config;

import com.fibanez.jersey2.security.AuthFilter;
import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

public class AppResourceConfig extends ResourceConfig {
    public AppResourceConfig() {
        // Guice ResourceConfig
        register(new ContainerLifecycleListener() {
            public void onStartup(Container container) {
                ServletContainer servletContainer = (ServletContainer)container;
                ServiceLocator serviceLocator = container.getApplicationHandler().getServiceLocator();
                GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
                GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
                Injector injector = (Injector) servletContainer.getServletContext().getAttribute(Injector.class.getName());
                guiceBridge.bridgeGuiceInjector(injector);
            }
            public void onReload(Container container) {
            }
            public void onShutdown(Container container) {
            }
        });
        register(AuthFilter.class);
    }

}
