package com.accionmfb.expertbus.logger;

import com.accionmfb.expertbus.config.ExpertBusObjectMapper;
import com.accionmfb.expertbus.config.IrisConnectionProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisConnectionProperties.class)
public class IrisApiLogger {

    private final IrisConnectionProperties irisConnectionProperties;
    private final ExpertBusObjectMapper objectMapper = new ExpertBusObjectMapper();

    public void logIrisApiRequest(String url, String method, Object body, Map<String, String> headers, Map<String, Object> params){
        String requestLoggingStyle = irisConnectionProperties.getRequestLogging();
        log.info("============================================ IRIS API REQUEST ============================================");
        log.info("URL: {}", url);
        log.info("Http Method: {}", method.toUpperCase());
        log.info("Request headers: {}", objectMapper.stringify(headers));
        log.info("Request Parameters: {}", objectMapper.stringify(params));
        Object requestBody;
        if(body instanceof String){
            requestBody = objectMapper.objectify((String) body);
        }else{
            requestBody = body;
        }
        String requestBodyPrefixLog = "Request Body: {}";
        if(requestLoggingStyle.equalsIgnoreCase("pretty")) {
            log.info(requestBodyPrefixLog, objectMapper.prettyStringify(requestBody));
        }else{
            log.info(requestBodyPrefixLog, objectMapper.stringify(requestBody));
        }
        log.info("===========================================================================================================");
    }

    public void logIrisApiResponse(ResponseEntity<String> httpResponse){
        String responseLoggingStyle = irisConnectionProperties.getResponseLogging();

        log.info("======================================== IRIS API SUCCESS RESPONSE ========================================");
        log.info("Http Status: {}", httpResponse.getStatusCodeValue());
        log.info("Http Status Text: {}", httpResponse.getStatusCode().toString());
        log.info("Http Headers: {}", objectMapper.stringify(httpResponse.getHeaders()));
        log.info("Http Cookies: {}", objectMapper.stringify(Collections.EMPTY_LIST));
        String responseBodyPrefixLog = "Http Response body: {}";
        if(responseLoggingStyle.equalsIgnoreCase("pretty")) {
            log.info(responseBodyPrefixLog, objectMapper.prettyStringify(objectMapper.objectify(httpResponse.getBody())));
        }else{
            log.info(responseBodyPrefixLog, objectMapper.stringify(objectMapper.objectify(httpResponse.getBody())));
        }
        log.info("===========================================================================================================");
    }

    public void logIrisApiExceptionResponse(HttpStatusCodeException httpResponse){
        String responseLoggingStyle = irisConnectionProperties.getResponseLogging();

        log.info("======================================= IRIS API EXCEPTION RESPONSE =======================================");
        log.info("Http Status: {}", httpResponse.getStatusCode().value());
        log.info("Http Status Text: {}", httpResponse.getStatusCode());
        log.info("Http Headers: {}", objectMapper.stringify(httpResponse.getResponseHeaders()));
        log.info("Http Cookies: {}", objectMapper.stringify(Collections.EMPTY_LIST));
        String responseBodyPrefixLog = "Http Response body: {}";
        if(responseLoggingStyle.equalsIgnoreCase("pretty")) {
            log.info(responseBodyPrefixLog, objectMapper.prettyStringify(objectMapper.objectify(httpResponse.getResponseBodyAsString())));
        }else{
            log.info(responseBodyPrefixLog, objectMapper.stringify(objectMapper.objectify(httpResponse.getResponseBodyAsString())));
        }
        log.info("===========================================================================================================");
    }
}
