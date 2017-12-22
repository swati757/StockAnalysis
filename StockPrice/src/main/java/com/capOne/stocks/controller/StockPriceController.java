package com.capOne.stocks.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capOne.stocks.domain.AverageStockPrice;
import com.capOne.stocks.domain.BusyDay;
import com.capOne.stocks.domain.DailyMaxProfit;
import com.capOne.stocks.domain.Stock;
import com.capOne.stocks.exception.StocksException;
import com.capOne.stocks.service.StockService;
import com.capOne.stocks.util.DateUtil;

@RestController
@RequestMapping("/stocks/")
public class StockPriceController {

    private static final String END_DATE = "2017-07-31";

	private static final String START_DATE = "2017-01-01";

	@Autowired
    private StockService stockService;

    private static final Logger log = LoggerFactory.getLogger(StockPriceController.class);

    @RequestMapping(path = "/average/{tickers}", method = RequestMethod.GET, produces = "application/json")
    public Map<String, List<AverageStockPrice>> getAverageStockPrice(
    		@PathVariable String[] tickers,
            @RequestParam(defaultValue=START_DATE) String startDate,
            @RequestParam(defaultValue=END_DATE) String endDate)
            throws JSONException, StocksException, ParseException {
        log.info("In Average Stock Price call for " + tickers);

        // validate the input
        String result = validateInput(tickers, startDate, endDate);

        if (result.length() > 0) {
            throw new StocksException(HttpStatus.BAD_REQUEST, result);
        }

        // getStocks
        List<Stock> stockList = stockService.getStocks(tickers, startDate, endDate);

        return stockService.getAveragePrices(stockList);

    }

    private String validateInput(String[] tickers, String startDate, String endDate) {
        StringBuilder message = new StringBuilder();

        if (tickers == null || tickers.length == 0) {
            message.append("No tickeres are provided");
        }

        if (startDate == null || startDate.length() == 0) {
            message.append("Missing start Date");
        }

        if (endDate == null || endDate.length() == 0) {
            message.append("missing end date");
        }

        validateDate(startDate, message, "start date");
        validateDate(endDate, message, "end date");

        // check if start date is before end date
        try {
            Date date1 = new SimpleDateFormat(DateUtil.DATE_FORMAT).parse(startDate);
            Date date2 = new SimpleDateFormat(DateUtil.DATE_FORMAT).parse(endDate);
            if (date1.after(date2)) {
                message.append("start date is after end date");
            }
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return message.toString();
    }

    private void validateDate(String dateString, StringBuilder message, String string) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_FORMAT);
            date = sdf.parse(dateString);
            if (!dateString.equals(sdf.format(date))) {
                date = null;
            }
        }
        catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            message.append(string + " is not in the right format");
        }
    }

    @RequestMapping(path = "/maxDailyProfit/{tickers}", method = RequestMethod.GET, produces = "application/json")
    public List<DailyMaxProfit> getMaxDailyProfit(
    		@PathVariable String[] tickers, 
            @RequestParam(defaultValue=START_DATE) String startDate,
            @RequestParam(defaultValue=END_DATE) String endDate) throws JSONException, StocksException, ParseException {
        log.info("In getMaxDailyProfit");

        // validate the input
        String result = validateInput(tickers, startDate, endDate);

        if (result.length() > 0) {
            throw new StocksException(HttpStatus.BAD_REQUEST, result);
        }

        List<Stock> stockList = stockService.getStocks(tickers, startDate, endDate);
        return stockService.getMaxDailyProfit(stockList);
    }

    @RequestMapping(path = "/busyDay/{tickers}", method = RequestMethod.GET, produces = "application/json")
    public List<BusyDay> getBusyDay(
    		@PathVariable String[] tickers, 
            @RequestParam(defaultValue=START_DATE) String startDate,
            @RequestParam(defaultValue=END_DATE) String endDate) throws JSONException, StocksException, ParseException {
        log.info("In getMaxDailyProfit");

        // validate the input
        String result = validateInput(tickers, startDate, endDate);

        if (result.length() > 0) {
            throw new StocksException(HttpStatus.BAD_REQUEST, result);
        }

        List<Stock> stockList = stockService.getStocks(tickers, startDate, endDate);
        return stockService.getBusyDays(stockList);
    }
}
