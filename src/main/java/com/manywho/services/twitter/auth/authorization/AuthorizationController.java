package com.manywho.services.twitter.auth.authorization;

import com.google.inject.Provider;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.controllers.AbstractAuthorizationController;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/authorization")
public class AuthorizationController extends AbstractAuthorizationController {
    private final AuthorizationManager manager;
    private final Provider<AuthenticatedWho> authenticatedWhoProvider;


    @Inject
    public AuthorizationController(AuthorizationManager manager, Provider<AuthenticatedWho> authenticatedWhoProvider) {
        this.manager = manager;
        this.authenticatedWhoProvider = authenticatedWhoProvider;
    }

    @POST
    @Override
    public ObjectDataResponse authorization(ObjectDataRequest objectDataRequest) {
        return manager.authorization(authenticatedWhoProvider.get(), objectDataRequest);
    }

    @Path("/group/attribute")
    @POST
    public ObjectDataResponse groupAttributes() {
        return manager.groupAttributes();
    }

    @Path("/group")
    @POST
    public ObjectDataResponse groups(ObjectDataRequest request) {
        return manager.groups(request);
    }

    @Path("/user/attribute")
    @POST
    public ObjectDataResponse userAttributes() {
        return manager.userAttributes();
    }

    @Path("/user")
    @POST
    public ObjectDataResponse users(ObjectDataRequest request) {
        return manager.users(request);
    }
}
