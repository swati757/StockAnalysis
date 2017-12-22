# Stock price analysis

Stock price analysis is a Rest project that provides analysis on the stock prices for any valid security that is availble in QUANDL . 

Source of data : https://www.quandl.com/

## Design System Thinking

This project is designed keeping in mind the REST API priciples and making use of Microservices principles .  


## Getting Started

Running the Stock price analysis has two dependencies:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven] (https://maven.apache.org/download.cgi)

Then to get started:

1. Clone the repo
2. Change directory to StockAnalysis/StockPrice/
3. Run `mvn clean install`
4. Start up the microservice with the following command 'java -jar target/stocks-0.0.1-SNAPSHOT.jar' inside the target folder.


### Description

There are three API's implemented as part of this project . 

1. Get avaerage open and close prices per month per ticker given one or many tickers for a start and end date . 
2. Get maximum daily profit for one or many given tickers.
3. Get the busy day for one or many given tickers.

### API documentation 

Base URL 

http://<host>/stocks

1. Average open and close prices 

Request type - GET 

End point - /average/{tickers}?start_date=yyyy-mm-dd&endDate=yyyy-mm-dd

Sample Response - 

{
  "GOOGL": [
    {
      "month": "2017-03",
      "averageClose": 853.86,
      "averageOpen": 853.79
    },
    {
      "month": "2017-04",
      "averageClose": 860.08,
      "averageOpen": 861.38
    }
  ],
  "COF": [
    {
      "month": "2017-03",
      "averageClose": 89.27,
      "averageOpen": 88.93
    },
    {
      "month": "2017-04",
      "averageClose": 83.41,
      "averageOpen": 83.24
    }
  ]
}

2. Max daily profit 
 
Request type - GET 

End point - /maxDailyProfit/{tickers}?start_date=yyyy-mm-dd&endDate=yyyy-mm-dd

Sample Response - 

[
  {
    "ticker": "GOOGL",
    "date": "2017-06-09",
    "averageProfit": "52.13"
  },
  {
    "ticker": "COF",
    "date": "2017-03-21",
    "averageProfit": "3.76"
  }
]

3. Busy Day 

Request type - GET 

End point - /maxDailyProfit/{tickers}?start_date=yyyy-mm-dd&endDate=yyyy-mm-dd

Sample Response - 

[
  {
    "ticker": "GOOGL",
    "averageVolume": 1832635.53,
    "volumes": [
      {
        "date": "2017-01-06",
        "volume": 2017097.0
      }
    ]
  },
  {
    "ticker": "COF",
    "averageVolume": 2552693.84,
    "volumes": [
      {
        "date": "2017-01-03",
        "volume": 3441067.0
      }
    ]
  }
]


### Example

1. Average open and close prices 

http://localhost:8080/stocks/average/GOOGL,COF?startDate=2017-01-01&endDate=2017-07-31

2. Max daily profit 

http://localhost:8080/stocks/maxDailyProfit/COF,GOOGL?startDate=2017-01-01&endDate=2017-07-31

3. Busy Day 

http://localhost:8080/stocks/busyDay/GOOGL,COF?startDate=2017-01-01&endDate=2017-01-31


## Stock Price Analysis Support
If you run into issues with Stock Price Analysisyou can file an issue on the open source Stock Price Analysis GitHub repo](https://github.com/swati757/StockAnalysis) or contact swati757@gmail.com
