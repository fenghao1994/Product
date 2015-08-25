package com.singularityclub.shopping.Model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by fenghao on 2015/8/24.
 * 每一项商品
 */
@JsonIgnoreProperties({"brief", "character", "area", "weight", "main_classify_id", "sub_classify_id", "deleted_at", "shop_id", "is_common", "created_at", "updated_at"})
public class ProductionItem {

    public String id;
//    public String urlImg;
    public String name;
    public String price;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   /* public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
