package com.manywho.services.twitter.status;

import com.google.inject.Inject;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.twitter.ServiceConfiguration;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class UpdateStatusCommand implements ActionCommand<ServiceConfiguration, UpdateStatus, UpdateStatus.Input, UpdateStatus.Output> {
    private Twitter twitter;

    @Inject
    public UpdateStatusCommand(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public ActionResponse<UpdateStatus.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, UpdateStatus.Input input) {
        twitter4j.Status status;

        try {
            status = twitter.tweets().updateStatus(input.getText());
        } catch (TwitterException e) {
            throw new RuntimeException("Error updating status", e);
        }

        UpdateStatus.Output output = new UpdateStatus.Output(new Status(String.valueOf(status.getId()), status.getText()));
        return new ActionResponse<>(output);
    }
}
