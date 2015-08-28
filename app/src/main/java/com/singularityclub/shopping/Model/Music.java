package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Howe on 2015/8/27.
 */
@JsonIgnoreProperties({"id", "product_id", "deleted_at", "created_at", "updated_at", "avatar_content_type", "avatar_file_size", "avatar_updated_at", "avatar_file_name"})
public class Music {
    @JsonProperty("url")
    public String audioUrl;
    @JsonProperty("link")
    public String name;

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
