package com.singularityclub.shopping.Model;

import java.util.Date;

/**
 * Created by fenghao on 2015/8/27.
 */
public class Action {

    public int customerId;
    public Date startTime;
    public Date endTime;
    public int extraId;
    public int extraType;
    public float totalMinutes;

    public float getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(float totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getExtraId() {
        return extraId;
    }

    public void setExtraId(int extraId) {
        this.extraId = extraId;
    }

    public int getExtraType() {
        return extraType;
    }

    public void setExtraType(int extraType) {
        this.extraType = extraType;
    }
}
