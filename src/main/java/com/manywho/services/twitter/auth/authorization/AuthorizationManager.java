package com.manywho.services.twitter.auth.authorization;

import com.google.inject.Inject;
import com.manywho.sdk.api.AuthorizationType;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.sdk.services.types.system.$User;
import com.manywho.services.twitter.guice.TwitterProvider;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

public class AuthorizationManager {
    private final AuthorizationRepository repository;
    private final TypeBuilder typeBuilder;
    private TwitterProvider twitterProvider;

    @Inject
    public AuthorizationManager(AuthorizationRepository repository, TypeBuilder typeBuilder, TwitterProvider twitterProvider) {
        this.repository = repository;
        this.typeBuilder = typeBuilder;
        this.twitterProvider = twitterProvider;
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
                Twitter twitter = twitterProvider.get();
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
