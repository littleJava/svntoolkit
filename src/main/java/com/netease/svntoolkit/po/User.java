package com.netease.svntoolkit.po;

import org.simpleframework.xml.Element;

public class User {
    @Element
    private String name;
    @Element
    private String password;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
