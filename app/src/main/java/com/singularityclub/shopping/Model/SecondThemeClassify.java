package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fenghao on 2015/8/28.
 */
@JsonIgnoreProperties({"two_dimension_code", "brief_des", "detail_des", "parent_id", "deleted_at", "shop_id", "is_common", "created_at", "updated_at"})
public class SecondThemeClassify implements Serializable{
    @JsonProperty("id")
    public String secondThemeId;
    @JsonProperty("name")
    public String secondThemeName;

    public String getSecondThemeId() {
        return secondThemeId;
    }

    public void setSecondThemeId(String secondThemeId) {
        this.secondThemeId = secondThemeId;
    }

    public String getSecondThemeName() {
        return secondThemeName;
    }

    public void setSecondThemeName(String secondThemeName) {
        this.secondThemeName = secondThemeName;
    }
}
