package com.manywho.services.twitter.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class TwitterProvider implements Provider<Twitter> {

    @Inject
    public TwitterProvider() {}

    @Override
    public Twitter get() {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(System.getenv("OAUTH_CLIENT_ID"), System.getenv("OAUTH_CLIENT_SECRET"));
        return twitter;
    }
}
