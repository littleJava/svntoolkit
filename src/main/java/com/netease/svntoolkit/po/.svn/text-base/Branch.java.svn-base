package com.netease.svntoolkit.po;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class Branch {
    @Element
    private String url;
    @Element
    private long startRevision;
    @Element(required=false)
    private long endRevision = -1;
    @ElementList(required=false)
    private List<String> targetUsers;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public long getStartRevision() {
        return startRevision;
    }
    public void setStartRevision(long startRevision) {
        this.startRevision = startRevision;
    }
    public long getEndRevision() {
        return endRevision;
    }
    public void setEndRevision(long endRevision) {
        this.endRevision = endRevision;
    }
    public boolean isTargetUser(String userName){
        if (targetUsers == null||targetUsers.size() == 0) {
            return true;
        }else {
            return targetUsers.contains(userName);
        }
    }
    
}
