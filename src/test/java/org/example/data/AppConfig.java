package org.example.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    protected static FileInputStream file;
    protected static Properties properties;

    static {
        try {
            file = new FileInputStream("src/test/resources/app.properties");
            properties = new Properties();
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public AppConfig(){}

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
