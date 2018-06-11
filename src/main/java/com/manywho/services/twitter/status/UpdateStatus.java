package com.manywho.services.twitter.status;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;

@Action.Metadata(name = "Update Status", summary = "What's happening?", uri = "status-update")
public class UpdateStatus {
    public static class Input {
        @Action.Input(name = "Text", contentType = ContentType.String, required = true)
        private String text;

        public String getText() {
            return text;
        }
    }

    public static class Output {
        @Action.Output(name = "Status", contentType = ContentType.Object)
        private Status status;

        public Output(Status status) {
            this.status = status;
        }
    }
}
