package com.manywho.services.twitter.status;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = StatusType.NAME, summary = "Status")
public class StatusType implements Type {
    public final static String NAME = "Status";

    public StatusType(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    @Type.Identifier
    @Type.Property(name = "ID", contentType = ContentType.Number)
    private Long id;

    @Type.Property(name = "Text", contentType = ContentType.String)
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
