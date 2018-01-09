package com.fibanez.jersey2;

import com.fibanez.jersey2.config.MainGuiceModule;
import com.fibanez.jersey2.config.RestServletModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Collection;

public class AppGuiceServletContextListener extends GuiceServletContextListener {

    private static final String CLEAN_REGEX = "\\r+|\\n+|\\s+";
    private String propertyFilePath;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final String propertyInitParameter = "PropertyFilePath";
        propertyFilePath = servletContextEvent.getServletContext().getInitParameter(propertyInitParameter);

        super.contextInitialized(servletContextEvent);
    }

    @Override
    protected Injector getInjector() {
        final String filterName = "GuiceFilter";
        final String packageParam = "jersey.config.server.provider.packages";
        final String classnameParam = "jersey.config.server.provider.classnames";

        return Guice.createInjector(
                Stage.PRODUCTION
                , new MainGuiceModule(propertyFilePath)
                , new RestServletModule() {
                    @Override
                    protected void configureServlets() {
                        Collection<String> urlPattern = getUrlPatternMappings();
                        String[] packages = getInitParams(packageParam);
                        String[] classnames = getInitParams(classnameParam);

                        rest(urlPattern).packages(packages).classnames(classnames).build();
                    }

                    private FilterRegistration getFilterRegistration() {
                        ServletContext servletContext = getServletContext();
                        return servletContext.getFilterRegistration(filterName);
                    }
                    private Collection<String> getUrlPatternMappings() {
                        return getFilterRegistration().getUrlPatternMappings();
                    }
                    private String[] getInitParams(String initParam) {
                        if (initParam == null || initParam.isEmpty()) {
                            return new String[]{};
                        }
                        String paramValues = getFilterRegistration().getInitParameter(initParam);
                        paramValues = paramValues.replaceAll(CLEAN_REGEX, "");
                        return paramValues.split(",");
                    }
                }
        );
    }
}