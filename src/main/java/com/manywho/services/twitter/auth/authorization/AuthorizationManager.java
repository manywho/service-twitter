package com.manywho.services.twitter.auth.authorization;

import com.google.inject.Inject;
import com.manywho.sdk.api.AuthorizationType;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.sdk.services.types.system.$User;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

public class AuthorizationManager {
    private final AuthorizationRepository repository;
    private final TypeBuilder typeBuilder;
    private final Twitter twitter;

    @Inject
    public AuthorizationManager(AuthorizationRepository repository, TypeBuilder typeBuilder, Twitter twitter) {
        this.repository = repository;
        this.typeBuilder = typeBuilder;
        this.twitter = twitter;
    }

    public ObjectDataResponse authorization(AuthenticatedWho authenticatedWho, ObjectDataRequest request) {
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
        user.setLoginUrl("");

        if (status.equals("401")) {
            RequestToken requestToken;
            try {
                Twitter twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(System.getenv("OAUTH_CLIENT_ID"), System.getenv("OAUTH_CLIENT_SECRET"));
                requestToken = twitter.getOAuthRequestToken();
            } catch (TwitterException e) {
                throw new RuntimeException("Unable to get the OAuth1.0a request token from Twitter", e);
            }

            repository.putTokenSecret(requestToken.getToken(), requestToken.getTokenSecret());

            user.setLoginUrl(requestToken.getAuthorizationURL());
        }

        return new ObjectDataResponse(typeBuilder.from(user));
    }

    public ObjectDataResponse groupAttributes() {
        throw new RuntimeException("Specifying group restrictions isn't yet supported in the Twitter Service");
    }

    public ObjectDataResponse groups(ObjectDataRequest request) {
        throw new RuntimeException("Specifying group restrictions isn't yet supported in the Twitter Service");
    }

    public ObjectDataResponse userAttributes() {
        throw new RuntimeException("Specifying user restrictions isn't yet supported in the Twitter Service");
    }

    public ObjectDataResponse users(ObjectDataRequest request) {
        throw new RuntimeException("Specifying user restrictions isn't yet supported in the Twitter Service");
    }
}
