package com.capOne.stocks.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StocksException extends Exception {

    HttpStatus status;

    int code;

    /**
     *
     * @param status
     * @param message
     */

    public StocksException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public StocksException() {
    }

}
