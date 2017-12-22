package com.capOne.stocks.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capOne.stocks.domain.AverageStockPrice;
import com.capOne.stocks.domain.BusyDay;
import com.capOne.stocks.domain.DailyMaxProfit;
import com.capOne.stocks.domain.Stock;
import com.capOne.stocks.domain.StockVolume;
import com.capOne.stocks.dto.OutgoingRequest;
import com.capOne.stocks.exception.StocksException;
import com.capOne.stocks.util.DateUtil;
import com.capOne.stocks.util.RequestType;

@Service
public class StockService {

    @Value("${quandl.service.uri}")
    private String uri;

    @Value("${quandl.api.key}")
    private String apiKey;

    @Value("${quandl.service.timeout}")
    private int timeout;

    @Autowired
    private ExternalAPICall externalAPICall;

    private static final Logger log = LoggerFactory.getLogger(StockService.class);

    /**
     * Makes call to external API. Converts to Stock Model
     * 
     * @param tickers
     * @param startDate
     * @param endDate
     * @return
     * @throws StocksException
     * @throws JSONException
     * @throws ParseException
     */
    @Cacheable
    // @Retryable(value = {StocksException.class}, maxAttempts = 3)
    public List<Stock> getStocks(String[] tickers, String startDate, String endDate)
            throws StocksException, JSONException, ParseException {
        log.info("in getStocks");

        // Get the data by invoking external API.
        ResponseEntity<String> response = getStocksFromExternalApi(tickers, startDate, endDate);

        // convert to stock Model for easy processing
        List<Stock> stockList = convertToStockModel(response);

        log.info(stockList.size() + " were retrieved");

        return stockList;
    }

    private List<Stock> convertToStockModel(ResponseEntity<String> response) throws JSONException, ParseException {
        List<Stock> stockList = new ArrayList<>();
        JSONArray data = new JSONObject(response.getBody()).getJSONObject("datatable").getJSONArray("data");

        // for each data element get the array data and convert to stock
        for (int i = 0; i < data.length(); i++) {
            JSONArray s = (JSONArray) data.get(i);
            Stock stocks = new Stock(s.get(0).toString(), DateUtil.removeddFromDate(s.get(1).toString()),
                    (Double) s.get(2), (Double) s.get(3), (Double) s.get(4), (Double) s.get(5), (Double) s.get(6),
                    s.get(1).toString());
            stockList.add(stocks);
        }
        return stockList;
    }

    private ResponseEntity<String> getStocksFromExternalApi(String[] tickers, String startDate, String endDate)
            throws StocksException {
        log.info("StockService:getStocks" + " tickers: " + tickers.toString() + ", startDate: " + startDate
                + ", endDate: " + endDate);

        StringBuilder path = new StringBuilder("/PRICES.json?api_key=");
        path.append(apiKey);
        path.append("&ticker=");
        path.append(String.join(",", tickers));
        path.append("&date.gte=");
        path.append(startDate);
        path.append("&date.lt=");
        path.append(endDate);

        OutgoingRequest outgoingRequest = new OutgoingRequest.RequestBuilder(uri, timeout, RequestType.GET)
                .path(path.toString()).timeout(timeout).build();
        return externalAPICall.send(outgoingRequest);
    }

    /**
     * retrieves the average price by ticker and by month.
     * 
     * @param stockList
     * @return
     * @throws StocksException
     */
    public Map<String, List<AverageStockPrice>> getAveragePrices(List<Stock> stockList) throws StocksException {
        log.info("StockService:getAveragePrices");

        Map<String, List<AverageStockPrice>> averageStockPriceMap = new HashMap<String, List<AverageStockPrice>>();

        // group by the company name
        Map<String, List<Stock>> stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getTicker));

        stockMap.forEach((company, value) -> {

            List<AverageStockPrice> averageStockPriceList = new ArrayList<>();

            // group by month
            Map<String, List<Stock>> dateMap = value.stream().collect(Collectors.groupingBy(Stock::getDate));

            // Create average close and open for each month
            dateMap.forEach((k, v) -> {
                Double average = new Double(new DecimalFormat("##.##")
                        .format(v.stream().mapToDouble(Stock::getOpen).average().getAsDouble()));
                Double averageClose = new Double(new DecimalFormat("##.##")
                        .format(v.stream().mapToDouble(Stock::getClose).average().getAsDouble()));
                AverageStockPrice averageSockPrice = new AverageStockPrice(k, average, averageClose);
                averageStockPriceList.add(averageSockPrice);
            });

            averageStockPriceMap.put(company, averageStockPriceList);
        });

        return averageStockPriceMap;

    }

    public List<DailyMaxProfit> getMaxDailyProfit(List<Stock> stockList) throws StocksException {

        // group by the company name
        Map<String, List<Stock>> stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getTicker));

        List<DailyMaxProfit> dailyMaxProfits = new ArrayList<>();

        stockMap.forEach((key, value) -> {
            DailyMaxProfit dailyMaxProfit = new DailyMaxProfit();
            dailyMaxProfit.setTicker(key);
            Stock maxStock = value.stream().max(Comparator.comparingDouble(i -> (i.getHigh() - i.getLow()))).get();
            dailyMaxProfit.setDate(maxStock.getCurrentDate());
            dailyMaxProfit.setAverageProfit(new DecimalFormat("##.##").format(maxStock.getHigh() - maxStock.getLow()));
            dailyMaxProfits.add(dailyMaxProfit);
        });

        return dailyMaxProfits;
    }

    public List<BusyDay> getBusyDays(List<Stock> stockList) {
        List<BusyDay> busyDays = new ArrayList<>();
        // group by the company name
        Map<String, List<Stock>> stockMap = stockList.stream().collect(Collectors.groupingBy(Stock::getTicker));

        // for each company, find the busy day
        stockMap.forEach((key, values) -> {
            // average volume
            double averageVolume = new Double(new DecimalFormat("##.##")
                    .format(values.stream().mapToDouble(Stock::getVolume).average().getAsDouble()));

            // ten percent more than average
            double tenPercentHigherVolume = averageVolume + averageVolume * 0.1;

            // filter voumes below average. get custom volumes.
            List<StockVolume> volumes = new ArrayList<>();
            Stream<Stock> higherVolume = values.stream().filter(stock -> stock.getVolume() > tenPercentHigherVolume);
            higherVolume.forEach(s -> volumes.add(new StockVolume(s.getCurrentDate(), s.getVolume())));
            busyDays.add(new BusyDay(key, averageVolume, volumes));
        });

        return busyDays;
    }

}
