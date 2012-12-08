package com.netease.svntoolkit.po;

import org.simpleframework.xml.Element;

public class WorkingCopy {
    @Element
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
