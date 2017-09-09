package com.gu.cheng.scallop.api.model;

import java.io.Serializable;

/**
 * Created by gc on 2017/9/9.
 * Base 基类
 */
public class BaseRes<T> implements Serializable {
    private int status_code;
    private String msg;

    private T data;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
