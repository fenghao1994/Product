package com.singularityclub.shopping.Model;

/**
 * Created by fenghao on 2015/8/24.
 */
public class MainClassify {
    public String mainClassifyId;
    public String mainClassifyName;

    public MainClassify(String mainClassifyId, String mainClassifyName) {
        this.mainClassifyId = mainClassifyId;
        this.mainClassifyName = mainClassifyName;
    }

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
