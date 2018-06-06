package com.manywho.services.twitter.configuration.reader;


public interface ServiceConfigurationInterface {
    String get(String key);
    boolean has(String key);
}
