package com.singularityclub.shopping.Model;

/**
 * Created by fenghao on 2015/8/24.
 * 每一项商品
 */
public class ProductionItem {

    public String id;
    public String urlImg;
    public String name;
    public String price;

    public ProductionItem(String id, String urlImg, String name, String price) {
        this.id = id;
        this.urlImg = urlImg;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

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
