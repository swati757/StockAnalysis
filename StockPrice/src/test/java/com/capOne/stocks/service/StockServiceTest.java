package com.capOne.stocks.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.capOne.stocks.domain.AverageStockPrice;
import com.capOne.stocks.domain.BusyDay;
import com.capOne.stocks.domain.DailyMaxProfit;
import com.capOne.stocks.domain.Stock;
import com.capOne.stocks.dto.OutgoingRequest;
import com.capOne.stocks.exception.StocksException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StockService.class)
public class StockServiceTest {

    @InjectMocks
    StockService stockService;

    @Mock
    ExternalAPICall externalApiCall;
    
    String responseString = "{\"datatable\":{\"data\":[[\"GOOGL\",\"2017-01-03\",800.62,811.435,796.89,808.01,1959033.0,0.0,1.0,800.62,811.435,796.89,808.01,1959033.0],[\"GOOGL\",\"2017-01-04\",809.89,813.43,804.11,807.77,1515339.0,0.0,1.0,809.89,813.43,804.11,807.77,1515339.0],[\"GOOGL\",\"2017-01-05\",807.5,813.74,805.92,813.02,1340535.0,0.0,1.0,807.5,813.74,805.92,813.02,1340535.0],[\"GOOGL\",\"2017-01-06\",814.99,828.96,811.5,825.21,2017097.0,0.0,1.0,814.99,828.96,811.5,825.21,2017097.0],[\"GOOGL\",\"2017-01-09\",826.37,830.43,821.62,827.18,1408924.0,0.0,1.0,826.37,830.43,821.62,827.18,1408924.0],[\"GOOGL\",\"2017-01-10\",827.07,829.41,823.14,826.01,1197442.0,0.0,1.0,827.07,829.41,823.14,826.01,1197442.0],[\"GOOGL\",\"2017-01-11\",826.62,829.9,821.47,829.86,1325394.0,0.0,1.0,826.62,829.9,821.47,829.86,1325394.0],[\"GOOGL\",\"2017-01-12\",828.38,830.38,821.01,829.53,1350308.0,0.0,1.0,828.38,830.38,821.01,829.53,1350308.0],[\"GOOGL\",\"2017-01-13\",831.0,834.65,829.52,830.94,1290182.0,0.0,1.0,831.0,834.65,829.52,830.94,1290182.0],[\"GOOGL\",\"2017-01-17\",830.0,830.18,823.2001,827.46,1440905.0,0.0,1.0,830.0,830.18,823.2001,827.46,1440905.0],[\"GOOGL\",\"2017-01-18\",829.8,829.81,824.08,829.02,1027698.0,0.0,1.0,829.8,829.81,824.08,829.02,1027698.0],[\"GOOGL\",\"2017-01-19\",829.0,832.9999,823.96,824.37,1070454.0,0.0,1.0,829.0,832.9999,823.96,824.37,1070454.0],[\"GOOGL\",\"2017-01-20\",829.09,829.24,824.6,828.17,1306183.0,0.0,1.0,829.09,829.24,824.6,828.17,1306183.0],[\"GOOGL\",\"2017-01-23\",831.61,845.5428,828.7,844.43,2457377.0,0.0,1.0,831.61,845.5428,828.7,844.43,2457377.0],[\"GOOGL\",\"2017-01-24\",846.98,851.52,842.28,849.53,1688375.0,0.0,1.0,846.98,851.52,842.28,849.53,1688375.0],[\"GOOGL\",\"2017-01-25\",853.55,858.7936,849.74,858.45,1662148.0,0.0,1.0,853.55,858.7936,849.74,858.45,1662148.0],[\"GOOGL\",\"2017-01-26\",859.05,861.0,850.5201,856.98,3493251.0,0.0,1.0,859.05,861.0,850.5201,856.98,3493251.0],[\"GOOGL\",\"2017-01-27\",859.0,867.0,841.9,845.03,3752497.0,0.0,1.0,859.0,867.0,841.9,845.03,3752497.0],[\"GOOGL\",\"2017-01-30\",837.06,837.23,821.03,823.83,3516933.0,0.0,1.0,837.06,837.23,821.03,823.83,3516933.0],[\"GOOGL\",\"2017-01-31\",819.5,823.07,813.4,820.19,2020180.0,0.0,1.0,819.5,823.07,813.4,820.19,2020180.0]],\"columns\":[{\"name\":\"ticker\",\"type\":\"String\"},{\"name\":\"date\",\"type\":\"Date\"},{\"name\":\"open\",\"type\":\"BigDecimal(34,12)\"},{\"name\":\"high\",\"type\":\"BigDecimal(34,12)\"},{\"name\":\"low\",\"type\":\"BigDecimal(34,12)\"},{\"name\":\"close\",\"type\":\"BigDecimal(34,12)\"},{\"name\":\"volume\",\"type\":\"BigDecimal(37,15)\"},{\"name\":\"ex-dividend\",\"type\":\"BigDecimal(42,20)\"},{\"name\":\"split_ratio\",\"type\":\"double\"},{\"name\":\"adj_open\",\"type\":\"BigDecimal(50,28)\"},{\"name\":\"adj_high\",\"type\":\"BigDecimal(50,28)\"},{\"name\":\"adj_low\",\"type\":\"BigDecimal(50,28)\"},{\"name\":\"adj_close\",\"type\":\"BigDecimal(50,28)\"},{\"name\":\"adj_volume\",\"type\":\"double\"}]},\"meta\":{\"next_cursor_id\":null}}";

    String[] tickers = {"COF"};
    String startDate = "01-01-2017";
    String endDate = "01-31-2017";
    
    ResponseEntity<String> response = new ResponseEntity<String>(responseString , HttpStatus.OK);
    
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getStocks() throws Exception {
        when(externalApiCall.send(any(OutgoingRequest.class))).thenReturn(response);
        List<Stock> stocks = stockService.getStocks(tickers, startDate, endDate);
        assertEquals(stocks.size(), 20);
    }
    
    @Test(expected=StocksException.class)
    public void getStocksException() throws Exception {
        when(externalApiCall.send(any(OutgoingRequest.class))).thenThrow(StocksException.class);
        stockService.getStocks(tickers, startDate, endDate);
    }
    
    
    
    @Test
    public void getAverageStocks() throws Exception {
    	
    	when(externalApiCall.send(any(OutgoingRequest.class))).thenReturn(response);
    	List<Stock> stocks = stockService.getStocks(tickers, startDate, endDate);
    	Map<String, List<AverageStockPrice>> averagePrices = stockService.getAveragePrices(stocks);
    	assertEquals(averagePrices.size(), 1);
    	List<AverageStockPrice> prices = averagePrices.get("GOOGL");
    	assertEquals(prices.get(0).getAverageClose(), new Double(829.85));
    	assertEquals(prices.get(0).getAverageOpen(), new Double(830.25));
    }
    
    @Test
    public void getBusyDay() throws Exception {
    	when(externalApiCall.send(any(OutgoingRequest.class))).thenReturn(response);
    	List<Stock> stocks = stockService.getStocks(tickers, startDate, endDate);
    	List<BusyDay> busyDay = stockService.getBusyDays(stocks);
    	assertEquals(busyDay.size(), 1);
    	assertEquals(busyDay.get(0).getAverageVolume(), new Double(1842012.75));
    	assertEquals(busyDay.get(0).getVolumes().size() , 4);
    }
    
    @Test
    public void getMaxDailyProfit() throws Exception {
       	when(externalApiCall.send(any(OutgoingRequest.class))).thenReturn(response);
    	List<Stock> stocks = stockService.getStocks(tickers, startDate, endDate);
    	List<DailyMaxProfit> maxProfits = stockService.getMaxDailyProfit(stocks);
    	assertEquals(maxProfits.size(), 1);
    	assertEquals(maxProfits.get(0).getAverageProfit(), "25.1");
    	assertEquals(maxProfits.get(0).getDate() , "2017-01-27");
    }
    
    
}


