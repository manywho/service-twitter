package com.manywho.services.twitter.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolProvider implements Provider<JedisPool> {

    @Inject
    public JedisPoolProvider() { }

    @Override
    public JedisPool get() {
        JedisPool pool = new JedisPool(System.getenv("REDIS_URL"));
        pool.addObjects(JedisPoolConfig.DEFAULT_MAX_IDLE);

        return pool;
    }
}
