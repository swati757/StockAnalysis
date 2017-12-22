package com.capOne.stocks.domain;

import java.util.List;

public class BusyDay {
    String ticker;

    Double averageVolume;

    List<StockVolume> volumes;

    public BusyDay(String ticker, Double avg, List<StockVolume> volumes) {
        this.ticker = ticker;
        this.averageVolume = avg;
        this.volumes = volumes;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(Double averageVolume) {
        this.averageVolume = averageVolume;
    }

    public List<StockVolume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<StockVolume> volumes) {
        this.volumes = volumes;
    }
}

/*
 * Copyright 2017 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of Capital One and is protected by law. It
 * may not be copied or distributed in any form or medium, disclosed to third parties, reverse engineered or used in any
 * manner without prior written authorization from Capital One.
 */
