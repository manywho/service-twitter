package com.manywho.services.twitter.status;

import com.google.inject.Inject;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.sdk.services.providers.AuthenticatedWhoProvider;
import com.manywho.services.twitter.ServiceConfiguration;
import com.manywho.services.twitter.twitter.TwitterFactory;
import twitter4j.TwitterException;

public class UpdateStatusCommand implements ActionCommand<ServiceConfiguration, UpdateStatus, UpdateStatus.Input, UpdateStatus.Output> {
    private TwitterFactory twitterFactory;
    private AuthenticatedWhoProvider authenticatedWhoProvider;

    @Inject
    public UpdateStatusCommand(AuthenticatedWhoProvider authenticatedWhoProvider, TwitterFactory twitterFactory) {
        this.authenticatedWhoProvider = authenticatedWhoProvider;
        this.twitterFactory = twitterFactory;
    }

    @Override
    public ActionResponse<UpdateStatus.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, UpdateStatus.Input input) {
        twitter4j.Status status;

        try {
            status = twitterFactory.get(authenticatedWhoProvider.get().getToken())
                    .tweets()
                    .updateStatus(input.getText());
        } catch (TwitterException e) {
            throw new RuntimeException("Error updating status", e);
        }

        UpdateStatus.Output output = new UpdateStatus.Output(new Status(String.valueOf(status.getId()), status.getText()));
        return new ActionResponse<>(output);
    }
}
