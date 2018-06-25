package com.manywho.services.twitter.twitter;

import com.google.inject.Inject;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;

public class TwitterFactory {
    private final Twitter twitter;

    @Inject
    public TwitterFactory(Twitter twitter) {
        this.twitter = twitter;
    }

    public Twitter get(String token) {
        String[] splitToken = token.split(":::");
        twitter.setOAuthAccessToken(new AccessToken(splitToken[0], splitToken[1]));

        return twitter;
    }
}
