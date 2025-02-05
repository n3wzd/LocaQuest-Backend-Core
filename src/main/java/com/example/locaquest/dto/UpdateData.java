package com.example.locaquest.dto;

import java.util.List;

public class UpdateData {
    private int maxLevel;
    private List<Integer> expLimitList;

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
    
    public void setExpLimitList(List<Integer> expLimitList) {
        this.expLimitList = expLimitList;
    }

    public List<Integer> getExpLimitList() {
        return expLimitList;
    }
}
