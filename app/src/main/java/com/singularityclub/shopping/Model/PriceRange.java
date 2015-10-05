package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by fenghao on 2015/9/29.
 */
@JsonIgnoreProperties({"deleted_at", "created_at", "updated_at", "shop_id"})
public class PriceRange {
    int id;
    String low;
    String high;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }
}
