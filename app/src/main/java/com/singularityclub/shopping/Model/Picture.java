package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Howe on 2015/8/27.
 */
@JsonIgnoreProperties({"id", "picture_id", "deleted_at", "created_at", "updated_at", "link", "avatar_content_type", "avatar_file_size", "avatar_updated_at", "product_id", "avatar_file_name"})
public class Picture {
    @JsonProperty("url")
    public String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
