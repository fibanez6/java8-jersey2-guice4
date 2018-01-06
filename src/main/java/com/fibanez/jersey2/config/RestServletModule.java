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

        public RestKeyBindingBuilderImpl(List<String> paths) {
            this.paths = paths;
        }

        private boolean checkIfPackageExistsAndLog(String packge) {
            boolean exists = false;
            String resourcePath = packge.replace(".", "/");
            URL resource = getClass().getClassLoader().getResource(resourcePath);
            if (resource != null) {
                exists = true;
                log.log(Level.INFO, "rest(" + paths + ").packages(" + packge + ")");
            } else {
                log.log(Level.INFO, "No Beans in '" + packge + "' found. Requests " + paths + " will fail.");
            }
            return exists;
        }

        @Override
        public void packages(String ... packages) {
            StringBuilder sb = new StringBuilder();

            for (String pkg: packages) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                checkIfPackageExistsAndLog(pkg);
                sb.append(pkg);
            }
            Map<String, String> params = new HashMap<>();
            params.put("javax.ws.rs.Application", AppResourceConfig.class.getCanonicalName());
            if (sb.length() > 0) {
                params.put("jersey.config.server.provider.packages", sb.toString());
            }
            bind(ServletContainer.class).in(Scopes.SINGLETON);
            for (String path: paths) {
                serve(path).with(ServletContainer.class, params);
            }
        }
    }

}