package com.liutao.model;

/**
 * @author: LIUTAO
 * @Date: Created in 2018/8/25  15:05
 * @Modified By:
 */
public class ResponseModel {
    private int code; //0:成功，1：失败
    private String msg;

    public ResponseModel() {
    }

    public ResponseModel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
