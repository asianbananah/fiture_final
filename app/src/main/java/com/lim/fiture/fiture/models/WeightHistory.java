package com.lim.fiture.fiture.models;

import java.util.Date;

/**
 * Created by User on 15/10/2018.
 */

public class WeightHistory {
    private String userId;
    private int weightInKg;
    private Date date;

    public WeightHistory() {
    }

    public WeightHistory(String userId, int weightInKg, Date date) {
        this.userId = userId;
        this.weightInKg = weightInKg;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWeightInKg() {
        return weightInKg;
    }

    public void setWeightInKg(int weightInKg) {
        this.weightInKg = weightInKg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
