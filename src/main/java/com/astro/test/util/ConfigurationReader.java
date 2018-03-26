package com.astro.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sahanlm on 3/23/18.
 */
public class ConfigurationReader {

    private static ConfigurationReader _instance;
    private Properties defaultConfigs;

    private ConfigurationReader() throws IOException {
        this.defaultConfigs = new Properties();
        InputStream inputStream = ConfigurationReader.class.getClassLoader().getResourceAsStream(Constants.DEFAULT_CONFIG_FILE);
        this.defaultConfigs.load(inputStream);
    }

    public static ConfigurationReader getInstance() throws IOException {
        if (_instance == null) {
            _instance = new ConfigurationReader();
        }

        return _instance;
    }

    public String getConfig(String key) {
        String envValue = System.getenv(key);
        if (envValue == null) {
            return this.defaultConfigs.getProperty(key);
        }
        return  envValue;
    }

}
