package com.ac.common.util;

public class CommonForm<T> {
    private T data;
    private Limit extLimit;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Limit getExtLimit() {
        return extLimit;
    }

    public void setExtLimit(Limit extLimit) {
        this.extLimit = extLimit;
    }
}
