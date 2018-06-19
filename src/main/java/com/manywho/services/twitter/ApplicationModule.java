package com.manywho.services.twitter;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.manywho.services.twitter.guice.*;
import redis.clients.jedis.JedisPool;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JedisPool.class).toProvider(JedisPoolProvider.class).in(Singleton.class);
    }
}
