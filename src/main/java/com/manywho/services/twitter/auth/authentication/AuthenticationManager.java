package com.manywho.services.twitter.auth.authentication;

import com.google.common.base.Strings;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import com.manywho.services.twitter.twitter.TwitterFactory;
import twitter4j.AccountSettings;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import javax.inject.Inject;

public class AuthenticationManager {
    private final AuthenticationRepository repository;
    private Twitter twitter;
    private TwitterFactory twitterFactory;

    @Inject
    public AuthenticationManager(AuthenticationRepository repository, Twitter twitter, TwitterFactory twitterFactory) {
        this.repository = repository;
        this.twitter = twitter;
        this.twitterFactory = twitterFactory;
    }

    public AuthenticatedWhoResult authentication(AuthenticationCredentials credentials) {

        // Get the temporarily-stored token secret so we can generate an access token
        String tokenSecret = repository.getTokenSecret(credentials.getToken());
        if (Strings.isNullOrEmpty(tokenSecret)) {
            throw new RuntimeException("No token secret could be found - the token times out after 5 minutes");
        }

        AccessToken accessToken;

        try {
            accessToken = twitter
                    .getOAuthAccessToken(new RequestToken(credentials.getToken(), tokenSecret), credentials.getVerifier());

        } catch (TwitterException e) {
            throw new RuntimeException("Unable to create the access token from Twitter: " + e.getMessage(), e);
        }

        if (accessToken == null) {
            throw new RuntimeException("No access token was given back from Twitter");
        }

        // Now we need to create this concatenated token + secret "token" so we can send it back and forth in one field
        String token = String.format("%s:::%s", accessToken.getToken(), accessToken.getTokenSecret());

        AccountSettings accountSettings;
        try {
            accountSettings = twitterFactory.create(token).users().getAccountSettings();
        } catch (TwitterException e) {
            throw new RuntimeException("Error fetching user settings", e);
        }

        return createAuthenticatedWhoResult(accountSettings, token);
    }

    private AuthenticatedWhoResult createAuthenticatedWhoResult(AccountSettings user, String token) {
        AuthenticatedWhoResult result = new AuthenticatedWhoResult();
        result.setDirectoryId("TWITTER");
        result.setDirectoryName("TWITTER");
        result.setEmail("none");
        result.setFirstName(user.getScreenName());
        result.setIdentityProvider("?");
        result.setLastName(user.getScreenName());
        result.setStatus(AuthenticatedWhoResult.AuthenticationStatus.Authenticated);
        result.setTenantName("?");
        result.setToken(token);
        result.setUserId(user.getScreenName());
        result.setUsername(user.getScreenName());

        return result;
    }
}
