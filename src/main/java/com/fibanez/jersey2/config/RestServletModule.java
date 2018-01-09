package com.fibanez.jersey2.config;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RestServletModule extends ServletModule {

    private static final Logger log = Logger.getLogger(RestServletModule.class.getCanonicalName());

    @Override
    abstract protected void configureServlets();

    protected RestKeyBindingBuilder rest(String... urlPatterns) {
        return new RestKeyBindingBuilderImpl(Arrays.asList(urlPatterns));
    }

    protected RestKeyBindingBuilder rest(Collection<String> urlPatterns) {
        if (urlPatterns instanceof List)
            return new RestKeyBindingBuilderImpl((List<String>) urlPatterns);
        else
            return new RestKeyBindingBuilderImpl(new ArrayList<>(urlPatterns));
    }

    private class RestKeyBindingBuilderImpl implements RestKeyBindingBuilder {
        List<String> paths;
        Map<String, String> params = new HashMap<>();

        public RestKeyBindingBuilderImpl(List<String> paths) {
            this.paths = paths;
            this.params.put("javax.ws.rs.Application", AppResourceConfig.class.getCanonicalName());
        }

        @Override
        public RestKeyBindingBuilder packages(String ... packages) {
            String values = validateResource("packages", packages);
            if (values.length() > 0) {
                params.put("jersey.config.server.provider.packages", values);
            }
            return this;
        }

        @Override
        public RestKeyBindingBuilder classnames(String ... classnames) {
            if (classnames.length > 0) {
                String values = String.join(",", classnames);
                params.put("jersey.config.server.provider.classnames", values);
            }
            return this;
        }

        @Override
        public void build() {
            bind(ServletContainer.class).in(Scopes.SINGLETON);
            for (String path: paths) {
                serve(path).with(ServletContainer.class, params);
            }
        }

        private String validateResource(String resourceName, String... resourcePackages) {
            StringBuilder sb = new StringBuilder();
            for (String pkg: resourcePackages) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                checkIfResourceExistsAndLog(resourceName, pkg);
                sb.append(pkg);
            }
            return sb.toString();
        }

        private boolean checkIfResourceExistsAndLog(String resourceName, String resourcePackage) {
            boolean exists = false;
            String resourcePath = resourcePackage.replace(".", "/");
            URL resourceURL = getClass().getClassLoader().getResource(resourcePath);
            if (resourceURL != null) {
                exists = true;
                log.log(Level.INFO, "\nrest(" + paths + ")."+resourceName+"(" + resourcePackage + ")");
            } else {
                log.log(Level.INFO, "\nNo Beans in '" + resourceName + "' found. Requests " + paths + " will fail.");
            }
            return exists;
        }
    }

}