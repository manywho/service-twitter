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

    public String getOauth2ClientId() {
        return this.serviceConfigurationDefault.get("oauth2.clientId");
    }

    public String getOauth2ClientSecret()
    {
        return this.serviceConfigurationDefault.get("oauth2.clientSecret");
    }


    public String getAuthorizationUrl() {
        return String.format("https://api.twitter.com/oauth/authenticate?client_id=%s", this.getOauth2ClientId());
    }

    public String getRedisUrl() {
        return this.serviceConfigurationDefault.get("redis.url");
    }
}
