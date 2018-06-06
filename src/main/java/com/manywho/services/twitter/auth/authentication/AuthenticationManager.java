package com.manywho.services.twitter.auth.authentication;

import com.google.common.base.Strings;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.services.twitter.AppConfiguration;
import com.manywho.services.twitter.configuration.ServiceConfiguration;
import twitter4j.AccountSettings;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import javax.inject.Inject;

public class AuthenticationManager {
    private final AuthenticationRepository repository;
    private final ConfigurationParser configurationParser;
    private final Twitter twitter;

    @Inject
    public AuthenticationManager(AuthenticationRepository repository, ConfigurationParser configurationParser, Twitter twitter) {
        this.repository = repository;
        this.configurationParser = configurationParser;
        this.twitter = twitter;
    }

    public AuthenticatedWhoResult authentication(AuthenticationCredentials credentials) {
        ServiceConfiguration configuration = configurationParser.from(credentials);

        // Get the temporarily-stored token secret so we can generate an access token
        String tokenSecret = repository.getTokenSecret(credentials.getToken());
        if (Strings.isNullOrEmpty(tokenSecret)) {
            throw new RuntimeException("No token secret could be found - the token times out after 5 minutes");
        }


        AccessToken accessToken;

        try {
            accessToken = twitter.getOAuthAccessToken(new RequestToken(credentials.getToken(), tokenSecret));
        } catch (TwitterException e) {
            throw new RuntimeException("Unable to get the access token from TWITTER: " + e.getMessage(), e);
        }

        if (accessToken == null) {
            throw new RuntimeException("No access token was given back from JIRA");
        }

        // Now we need to create this concatenated token + secret "token" so we can send it back and forth in one field
        String token = String.format("%s:::%s", accessToken.getToken(), accessToken.getTokenSecret());

        AccountSettings accountSettings = null;
        try {
            accountSettings = twitter.users().getAccountSettings();
        } catch (TwitterException e) {
            throw new RuntimeException("Error fetching user settings", e);
        }

        return createAuthenticatedWhoResult(accountSettings, token);

    }

    private AuthenticatedWhoResult createAuthenticatedWhoResult(AccountSettings user, String token) {
        AuthenticatedWhoResult result = new AuthenticatedWhoResult();
        result.setDirectoryId("JIRA");
        result.setDirectoryName("JIRA");
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
