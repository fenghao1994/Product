package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fenghao on 2015/8/24.
 */
@JsonIgnoreProperties({"brief_des", "detail_des", "parent_id", "deleted_at", "shop_id", "is_common", "created_at", "updated_at"})
public class MainClassify implements Serializable{
    @JsonProperty("id")
    public String mainClassifyId;
    @JsonProperty("name")
    public String mainClassifyName;


    public String getMainClassifyId() {
        return mainClassifyId;
    }

    public void setMainClassifyId(String mainClassifyId) {
        this.mainClassifyId = mainClassifyId;
    }

    public String getMainClassifyName() {
        return mainClassifyName;
    }

    public void setMainClassifyName(String mainClassifyName) {
        this.mainClassifyName = mainClassifyName;
    }
}
