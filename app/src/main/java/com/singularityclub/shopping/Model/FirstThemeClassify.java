package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fenghao on 2015/8/28.
 */
@JsonIgnoreProperties({"two_dimension_code", "brief_des", "detail_des", "parent_id", "deleted_at", "shop_id", "is_common", "created_at", "updated_at"})
public class FirstThemeClassify implements Serializable {
    @JsonProperty("id")
    public String firstThemeId;
    @JsonProperty("name")
    public String firstThemeName;

    public String getFirstThemeId() {
        return firstThemeId;
    }

    public void setFirstThemeId(String firstThemeId) {
        this.firstThemeId = firstThemeId;
    }

    public String getFirstThemeName() {
        return firstThemeName;
    }

    public void setFirstThemeName(String firstThemeName) {
        this.firstThemeName = firstThemeName;
    }
}
