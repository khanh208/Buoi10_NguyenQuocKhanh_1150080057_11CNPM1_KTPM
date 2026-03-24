package com.lab10.utils;

public final class DbConfig {
    private DbConfig() {
    }

    public static String getUrl() {
        return ConfigReader.get("db.url");
    }

    public static String getUsername() {
        return ConfigReader.get("db.username");
    }

    public static String getPassword() {
        return ConfigReader.get("db.password");
    }

    public static boolean isEnabled() {
        return ConfigReader.getBoolean("db.enabled");
    }
}
