package com.nutcracker.wedo.common.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by huh on 2016/8/16.
 */
@Slf4j
public class wedoConfig {

    private static final String DEFAULT_CONFIG_PARENT = "application";

    private Config config;

    private static wedoConfig instance = null;

    private wedoConfig() {
        if (specified()) {
            this.config = ConfigFactory.load();
        } else {
            this.config = ConfigFactory.defaultOverrides().withFallback(loadUserDirConfig());
        }
        printProperties(DEFAULT_CONFIG_PARENT);
    }

    private Config loadUserDirConfig() {
        Config defaultConfig = ConfigFactory.load();
        String userDir = System.getProperty("user.dir");
        log.debug("user.dir : {}", userDir);
        File configFile = new File(userDir + File.separator + "application.conf");
        return ConfigFactory
                .parseFile(configFile, ConfigParseOptions.defaults()).withFallback(defaultConfig);
    }

    private void printProperties(String parent) {
        if (this.config.hasPath(parent) == false) {
            return;
        }
        Config config = this.config.getConfig(parent);
        log.info("\n**************************application configuration******************************");
        String render = config.root().render(ConfigRenderOptions.defaults().setOriginComments(false));
        log.info(render);
        log.info("\n*********************************************************************************");
    }

    private boolean specified() {
        String resource = System.getProperty("config.resource");
        if (resource != null) {
            log.debug("config.resource : {}", resource);
            return true;
        }
        String file = System.getProperty("config.file");
        if (file != null) {
            log.debug("config.file : {}", file);
            return true;
        }
        String url = System.getProperty("config.url");
        if (url != null) {
            log.debug("config.url : {}", url);
            return true;
        }
        return false;
    }

    public static Properties getProperties() {
        return getInstance().properties(DEFAULT_CONFIG_PARENT);
    }

    public static Properties getProperties(String parent) {
        return getInstance().properties(parent);
    }

    public static String getProperty(String nodeName, String property) {
        Properties properties = getInstance().getProperties(nodeName);
        return properties.getProperty(property);
    }

    public static Map<String, String> toMap(Properties properties) {
        Map<String, String> maps = new HashMap<String, String>();
        Set<String> entrySet = properties.stringPropertyNames();
        for (String s : entrySet) {
            maps.put(s, properties.getProperty(s));
        }
        return maps;
    }

    private static wedoConfig getInstance() {
        synchronized (wedoConfig.class) {
            if (instance == null) {
                instance = new wedoConfig();
            }
        }
        return instance;
    }

    private Properties properties(String parentName) {
        Properties props = new Properties();
        if (this.config.hasPath(parentName) == false) {
            return props;
        }
        Config simpleConfig = this.config.getConfig(parentName);
        Set<Map.Entry<String, ConfigValue>> entrySet = simpleConfig.entrySet();
        for (Map.Entry<String, ConfigValue> entry : entrySet) {
            props.setProperty(entry.getKey(), simpleConfig.getString(entry.getKey()));
        }
        return props;
    }
}
