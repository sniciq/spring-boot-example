package com.ac.common.util;

/**
 * Json返回格式
 * @param <T>
 */
public class JsonResult<T> {
    private T data;
    private boolean success = true;
    private String msg = "";

    public JsonResult() { }

    public JsonResult(T t) {
        this.data = t;
    }

    public JsonResult(String msg) {
        this.msg = msg;
    }

    public JsonResult(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }

    public static<T> JsonResult<T> success() {
        return new JsonResult<T>();
    }

    public static<T> JsonResult<T> success(String msg) {
        return new JsonResult<T>(msg);
    }

    public static<T> JsonResult<T> error() {
        JsonResult r = new JsonResult<T>();
        r.setMsg("error");
        r.setSuccess(false);
        return r;
    }

    public static<T> JsonResult<T> error(String msg) {
        JsonResult r = new JsonResult<T>();
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return isSuccess() ? "success" : "fail";
    }
}
