package com.capOne.stocks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AverageStockPrice {
    private String month;

    private Double average_close;

    private Double average_open;

    public AverageStockPrice(String month, Double averageClose, Double averageOpen) {
        this.month = month;
        this.average_close = averageClose;
        this.average_open = averageOpen;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getAverageClose() {
        return average_close;
    }

    public void setAverageClose(Double averageClose) {
        this.average_close = averageClose;
    }

    public Double getAverageOpen() {
        return average_open;
    }

    public void setAverageOpen(Double averageOpen) {
        this.average_open = averageOpen;
    }

}
