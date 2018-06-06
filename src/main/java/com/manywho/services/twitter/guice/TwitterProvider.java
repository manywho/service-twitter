package com.manywho.services.twitter.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.manywho.services.twitter.AppConfiguration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;


public class TwitterProvider implements Provider<Twitter> {

    private AppConfiguration appConfiguration;

    @Inject
    public TwitterProvider(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public Twitter get() {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(appConfiguration.getOauth2ClientId(), appConfiguration.getOauth2ClientSecret());
        return twitter;
    }
}
