package com.tinysun.countit.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by YS CHOI on 2017-12-31.
 */

public class CountDataSavedTimeModelRealm extends RealmObject{

    @PrimaryKey
    private int idx;
    @Required
    private String savedTime;

    public int getIdx() { return idx; }
    public void setIdx(int idx) { this.idx = idx; }

    public String getSavedTime() { return savedTime; }
    public void setSavedTime(String savedTime) { this.savedTime = savedTime; }

}
