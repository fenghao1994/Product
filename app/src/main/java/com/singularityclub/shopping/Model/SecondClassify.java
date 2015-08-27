package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fenghao on 2015/8/24.
 * 二级分类
 */
@JsonIgnoreProperties({"brief_des", "detail_des", "parent_id", "deleted_at", "shop_id", "is_common", "created_at", "updated_at"})
public class SecondClassify implements Serializable{
    @JsonProperty("id")
    public String secondClassifyId;
    @JsonProperty("name")
    public String secondClassifyName;


    public String getSecondClassifyId() {
        return secondClassifyId;
    }

    public void setSecondClassifyId(String secondClassifyId) {
        this.secondClassifyId = secondClassifyId;
    }

    public String getSecondClassifyName() {
        return secondClassifyName;
    }

    public void setSecondClassifyName(String secondClassifyName) {
        this.secondClassifyName = secondClassifyName;
    }
}
