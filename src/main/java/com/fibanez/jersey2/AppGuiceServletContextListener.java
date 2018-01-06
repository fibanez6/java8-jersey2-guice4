package com.fibanez.jersey2;

import com.fibanez.jersey2.config.MainGuiceModule;
import com.fibanez.jersey2.config.RestServletModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Collection;

public class AppGuiceServletContextListener extends GuiceServletContextListener {

    private String propertyFilePath;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final String propertyInitParameter = "PropertyFilePath";
        propertyFilePath = servletContextEvent.getServletContext().getInitParameter(propertyInitParameter);

        super.contextInitialized(servletContextEvent);
    }

    @Override
    protected Injector getInjector() {
        final String urlPatternMapping = "GuiceFilter";
        final String restPathParam = "restPath";

        return Guice.createInjector(
                Stage.PRODUCTION
                , new MainGuiceModule(propertyFilePath)
                , new RestServletModule() {
                    @Override
                    protected void configureServlets() {
                        Collection<String> urlPattern = getUrlPatternMappings();
                        String restClassesPath = getRestClassesPath();

                        rest(urlPattern).packages(restClassesPath);
                    }

                    private Collection<String> getUrlPatternMappings() {
                        ServletContext servletContext = getServletContext();
                        return servletContext.getFilterRegistration(urlPatternMapping).getUrlPatternMappings();
                    }
                    private String getRestClassesPath() {
                        ServletContext servletContext = getServletContext();
                        return servletContext.getFilterRegistration(urlPatternMapping).getInitParameter(restPathParam);
                    }
                }
        );
    }
}