package com.singularityclub.shopping.Model;

/**
 * Created by fenghao on 2015/8/24.
 * 二级分类
 */
public class SecondClassify {
    public String secondClassifyId;
    public String secondClassifyName;

    public SecondClassify(String secondClassifyId, String secondClassifyName) {
        this.secondClassifyId = secondClassifyId;
        this.secondClassifyName = secondClassifyName;
    }

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
