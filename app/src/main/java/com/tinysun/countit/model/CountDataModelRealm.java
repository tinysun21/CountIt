package com.tinysun.countit.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by YS CHOI on 2017-12-31.
 */

public class CountDataModelRealm extends RealmObject{

    @PrimaryKey
    private int idx;
    @Required
    private String title;
    private int countNum;

    public int getIdx() { return idx; }
    public void setIdx(int idx) { this.idx = idx; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title;}

    public int getCountNum() {
        return countNum;
    }
    public void setCountNum(int countNum) { this.countNum = countNum; }

}
