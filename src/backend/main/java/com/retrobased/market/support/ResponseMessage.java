package com.retrobased.market.support;


import lombok.Getter;

@Getter
public class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }


}
