package com.manywho.services.twitter.guice;

import com.google.inject.Provider;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterProvider implements Provider<Twitter> {
    @Override
    public Twitter get() {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(System.getenv("OAUTH_CLIENT_ID"), System.getenv("OAUTH_CLIENT_SECRET"));
        return twitter;
    }

    public Twitter getWithToken(String requestToken) {
        String[] splitToken = requestToken.split(":::");
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(System.getenv("OAUTH_CLIENT_ID"), System.getenv("OAUTH_CLIENT_SECRET"));
        twitter.setOAuthAccessToken(new AccessToken(splitToken[0], splitToken[1]));
        return twitter;
    }
}
