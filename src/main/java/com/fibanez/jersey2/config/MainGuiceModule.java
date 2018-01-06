package com.fibanez.jersey2.config;

import com.fibanez.jersey2.service.TokenService;
import com.fibanez.jersey2.service.TokenServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class MainGuiceModule extends AbstractModule {

    private static final String CLASSPATH = "classpath:";

    private final String propertyFile;

    public MainGuiceModule(String propertyFile) {

        if (propertyFile.startsWith(CLASSPATH)) {
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource(propertyFile.substring(CLASSPATH.length()));
            this.propertyFile = url.getPath();
        } else {
            this.propertyFile = propertyFile;
        }
    }

    private void setProperties(){
        if(!Files.exists(Paths.get(propertyFile))) {
            return;
        }

        try {
            Properties properties = new Properties();
            properties.load(new FileReader(propertyFile));
            Names.bindProperties(binder(), properties);
        } catch (IOException ex) {
            System.out.println("CANNOT OPEN "+propertyFile+" FILE FOR READING");
            ex.printStackTrace();
        }
    }

    @Override
    protected void configure() {
        setProperties();

        bind(TokenService.class).to(TokenServiceImpl.class).in(Singleton.class);
    }
}