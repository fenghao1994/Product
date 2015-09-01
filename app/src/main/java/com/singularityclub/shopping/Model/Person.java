package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by fenghao on 2015/9/1.
 * 人格model
 */
@JsonIgnoreProperties({"deleted_at", "created_at", "updated_at"})
public class Person {

    public int id;
    public String name;
    @JsonProperty("content")
    public String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
