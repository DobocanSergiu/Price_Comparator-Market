package com.pricecomparator.market.DTO.Response;

public class HttpCode {
    private int code;
    private String message;

    public HttpCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public HttpCode(int code) {
        this.code = code;
        this.message = "";
    }

    public HttpCode() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
