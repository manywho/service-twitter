package com.manywho.services.twitter.status;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = StatusType.NAME, summary = "Status")
public class StatusType implements Type {
    public final static String NAME = "Status";

    public StatusType(String id, String text) {
        this.id = id;
        this.text = text;
    }

    @Type.Identifier
    @Type.Property(name = "ID", contentType = ContentType.String)
    private String id;

    @Type.Property(name = "Text", contentType = ContentType.String)
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
