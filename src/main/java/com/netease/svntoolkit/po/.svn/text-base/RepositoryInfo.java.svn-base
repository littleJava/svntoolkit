package com.netease.svntoolkit.po;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RepositoryInfo {
    @Element
    private User user;
    @Element
    private Branch branch;
    @Element(name="workingCopy")
    private WorkingCopy wc;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Branch getBranch() {
        return branch;
    }
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    public WorkingCopy getWc() {
        return wc;
    }
    public void setWc(WorkingCopy wc) {
        this.wc = wc;
    }
}
