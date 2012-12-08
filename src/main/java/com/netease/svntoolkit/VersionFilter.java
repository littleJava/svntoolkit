package com.netease.svntoolkit;

public interface VersionFilter<V> {
    public boolean filter(V param);
}
