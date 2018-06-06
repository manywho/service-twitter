package com.manywho.services.twitter.auth.authorization;

import com.google.inject.Inject;
import com.manywho.sdk.api.AuthorizationType;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.sdk.services.types.system.$User;
import com.manywho.services.twitter.AppConfiguration;
import com.manywho.services.twitter.configuration.ServiceConfiguration;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import java.util.UUID;


public class AuthorizationManager {
    private final AuthorizationRepository repository;
    private final ConfigurationParser configurationParser;
    private final TypeBuilder typeBuilder;
    private final Twitter twitter;

    @Inject
    public AuthorizationManager(AuthorizationRepository repository, ConfigurationParser configurationParser, TypeBuilder typeBuilder, Twitter twitter) {
        this.repository = repository;
        this.configurationParser = configurationParser;
        this.typeBuilder = typeBuilder;
        this.twitter = twitter;
    }

    public ObjectDataResponse authorization(AuthenticatedWho authenticatedWho, ObjectDataRequest request) {
        ServiceConfiguration configuration = configurationParser.from(request);

        String status;

        switch (request.getAuthorization().getGlobalAuthenticationType()) {
            case AllUsers:
                // If it's a public user (i.e. not logged in) then return a 401
                if (authenticatedWho.getUserId().equals("PUBLIC_USER")) {
                    status = "401";
                } else {
                    status = "200";
                }

                break;
            case Public:
                status = "200";
                break;
            case Specified:
            default:
                status = "401";
                break;
        }

        $User user = new $User();
        user.setDirectoryId("TWITTER");
        user.setDirectoryName("TWITTER");
        user.setAuthenticationType(AuthorizationType.Oauth);

        user.setStatus(status);
        user.setUserId("");

        if (status.equals("401")) {
            RequestToken requestToken;
            try {
                requestToken = twitter.getOAuthRequestToken();
            } catch (TwitterException e) {
                throw new RuntimeException("Unable to get the OAuth1.0a request token from Twitter", e);
            }


            repository.putTokenSecret(requestToken.getToken(), requestToken.getTokenSecret());

            user.setLoginUrl(requestToken.getAuthorizationURL());
        } else {
            user.setLoginUrl("ttps://api.twitter.com/oauth/authorize?oauth_token=123");
        }
        return new ObjectDataResponse(typeBuilder.from(user));
    }

    public ObjectDataResponse groupAttributes() {
        throw new RuntimeException("Specifying group restrictions isn't yet supported in the TWITTER Service");
    }

    public ObjectDataResponse groups(ObjectDataRequest request) {
        throw new RuntimeException("Specifying group restrictions isn't yet supported in the TWITTER Service");
    }

    public ObjectDataResponse userAttributes() {
        throw new RuntimeException("Specifying user restrictions isn't yet supported in the TWITTER Service");
    }

    public ObjectDataResponse users(ObjectDataRequest request) {
        throw new RuntimeException("Specifying user restrictions isn't yet supported in the TWITTER Service");
    }
}
