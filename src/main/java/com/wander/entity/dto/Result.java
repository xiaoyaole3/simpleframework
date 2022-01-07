package com.wander.entity.dto;

import lombok.Data;

@Data
public class Result<T> {
    // 请求的状态码
    private int code;
    // 请求结果的详情
    private String msg;
    // 请求返回的结果集
    private T data;

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
