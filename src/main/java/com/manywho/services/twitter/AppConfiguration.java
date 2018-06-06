package com.manywho.services.twitter;


import com.manywho.services.twitter.configuration.reader.ServiceConfigurationDefault;

import javax.inject.Inject;

public class AppConfiguration {
    private ServiceConfigurationDefault serviceConfigurationDefault;

    @Inject
    public AppConfiguration(ServiceConfigurationDefault serviceConfigurationDefault) {
        this.serviceConfigurationDefault = serviceConfigurationDefault;
    }

    public String getName() { return "twitter"; }

    public String getOauthClientId() {
        return this.serviceConfigurationDefault.get("oauth.clientId");
    }

    public String getOauthClientSecret()
    {
        return this.serviceConfigurationDefault.get("oauth.clientSecret");
    }


    public String getAuthorizationUrl() {
        return String.format("https://api.twitter.com/oauth/authenticate?client_id=%s", this.getOauthClientId());
    }

    public String getRedisUrl() {
        return this.serviceConfigurationDefault.get("redis.url");
    }
}
