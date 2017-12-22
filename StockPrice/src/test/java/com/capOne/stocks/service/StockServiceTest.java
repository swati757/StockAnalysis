package com.capOne.stocks.service;

import java.text.ParseException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.capOne.stocks.exception.StocksException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(StockService.class)
public class StockServiceTest {

    @Autowired
    StockService stockService;

    @InjectMocks
    ExternalAPICall externalApiCall;

    @Before
    public void setUp() {

    }

    @Test
    public void getStocks() throws StocksException, JSONException, ParseException {

        String[] tickers = {"COF"};
        String startDate = "01-01-2017";
        String endDate = "01-31-2017";

        // when()
        stockService.getStocks(tickers, startDate, endDate);

    }
}

/*
 * Copyright 2017 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of Capital One and is protected by law. It
 * may not be copied or distributed in any form or medium, disclosed to third parties, reverse engineered or used in any
 * manner without prior written authorization from Capital One.
 */
