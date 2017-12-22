package com.capOne.stocks.service;

import com.capOne.stocks.util.RetryOnFailStratergy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.capOne.stocks.dto.OutgoingRequest;
import com.capOne.stocks.exception.StocksException;

@Component
public class ExternalAPICall {

    private static final Logger log = LoggerFactory.getLogger(ExternalAPICall.class);

    /**
     * External call to get the stock prices.
     * 
     * @param outgoingRequest
     * @return
     * @throws StocksException
     */
    public ResponseEntity send(OutgoingRequest outgoingRequest) throws StocksException {

        log.info("ExternalAPICall:send" + outgoingRequest);
        RetryOnFailStratergy retry = new RetryOnFailStratergy();
        ResponseEntity response = null;
        while(retry.shouldRetry()){
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
                SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
                rf.setConnectTimeout(outgoingRequest.getTimeout());
                response =  restTemplate.getForEntity(outgoingRequest.getUri().toString() + outgoingRequest.getPath(),
                        String.class);
                return response;
            }
            catch (Exception e) {
                try{
                    System.out.println("in catch.....");
                    retry.errorOccured();
                } catch (RuntimeException e1) {
                    String msg = "Excepton in web service: " + outgoingRequest.getPath() + "; Exception Message:"
                            + outgoingRequest.getLogMsg() + ", Path:->" + e.getMessage();
                    log.error(msg);
                    throw new StocksException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
                } catch (Exception e1) {
                    String msg = "Excepton in web service: " + outgoingRequest.getPath() + "; Exception Message:"
                            + outgoingRequest.getLogMsg() + ", Path:->" + e.getMessage();
                    log.error(msg);
                    throw new StocksException(HttpStatus.INTERNAL_SERVER_ERROR, msg);
                }

            }
        }
        return response;
    }
}



