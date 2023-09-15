package com.accionmfb.expertbus.service.impl;

import com.accionmfb.expertbus.config.ExpertBusObjectMapper;
import com.accionmfb.expertbus.config.IrisConnectionProperties;
import com.accionmfb.expertbus.logger.IrisApiLogger;
import com.accionmfb.expertbus.payload.response.IrisHttpHeader;
import com.accionmfb.expertbus.payload.response.IrisHttpResponse;
import com.accionmfb.expertbus.service.IrisConnectionTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnMissingBean(value = IrisConnectionTemplate.class)
@EnableConfigurationProperties(value = IrisConnectionProperties.class)
public class IrisConnectionTemplateImpl implements IrisConnectionTemplate {

    private final IrisConnectionProperties irisConnectionProperties;
    private final ExpertBusObjectMapper objectMapper = new ExpertBusObjectMapper();
    private final IrisApiLogger logger;
    private final RestTemplate irisRestTemplate;


    @Override
    public String getForObject(String resourceUrl){
        return this.getForObject(resourceUrl, new HashMap<>(), new HashMap<>());
    }

    @Override
    public <T> T getForObject(String resourceUrl, Class<T> tClass){
         return objectMapper.objectify(this.getForObject(resourceUrl), tClass);
    }

    @Override
    public String getForObject(String resourceUrl, Map<String, String> headers){
        return this.getForObject(resourceUrl, headers, new HashMap<>());
    }

    @Override
    public <T> T getForObject(String resourceUrl, Map<String, String> headers, Class<T> tClass){
        return objectMapper.objectify(this.getForObject(resourceUrl, headers), tClass);
    }

    @Override
    public String getForObjectWithParams(String resourceUrl, Map<String, Object> params){
        return this.getForObject(resourceUrl, null, params);
    }

    @Override
    public <T> T getForObjectWithParams(String resourceUrl, Map<String, Object> params, Class<T> tClass){
        String result = this.getForObject(resourceUrl, null, params);
        return this.objectMapper.objectify(result, tClass);
    }

    @Override
    public String getForObject(String resourceUrl, Map<String, String> headers, Map<String, Object> params){
        String url = getDestinationResourcePath(resourceUrl);
        logger.logIrisApiRequest(url, HttpMethod.GET.name(), "", headers, params);
        HttpEntity<?> requestEntity = new HttpEntity<>(getHttpHeaders(headers));
        ResponseEntity<String> httpResponse;
        try {
            httpResponse = irisRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, getUriVariables(params));
            logger.logIrisApiResponse(httpResponse);
            return httpResponse.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException exception){
            String errorResponse = exception.getResponseBodyAsString();
            httpResponse = new ResponseEntity<>(errorResponse, exception.getStatusCode());
            logger.logIrisApiExceptionResponse(exception);
            return httpResponse.getBody();
        }
    }

    @SneakyThrows
    @Override
    public <T> IrisHttpResponse<T> getIrisEntity(String resourceUrl, Map<String, String> headers, Map<String, Object> params, Class<T> tClass){
        String responseBody = this.getForObject(resourceUrl, headers, params);
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject headerObject = jsonObject.getJSONObject("header");
        JSONObject bodyObject = jsonObject.getJSONObject("body");
        IrisHttpResponse<T> response = new IrisHttpResponse<>();
        IrisHttpHeader header = objectMapper.objectify(headerObject.toString(), IrisHttpHeader.class);
        if(tClass.isAssignableFrom(String.class)){
            response.setBody((T) responseBody);
            response.setHeader(header);
        }else{
            T body = objectMapper.objectify(bodyObject.toString(), tClass);
            response.setHeader(header);
            response.setBody(body);
        }
        return response;
    }

    @Override
    public <T> IrisHttpResponse<T> getIrisEntity(String resourceUrl, Map<String, String> headers, Class<T> tClass){
        return this.getIrisEntity(resourceUrl, headers, null, tClass);
    }

    @Override
    public String postForObject(String resourceUrl, Object body){
        return this.postForObject(resourceUrl, body, new HashMap<>(), new HashMap<>());
    }

    @Override
    public <T> T postForObject(String resourceUrl, Object body, Class<T> tClass){
        return this.objectMapper.objectify(this.postForObject(resourceUrl, body), tClass);
    }

    @Override
    public String postForObject(String resourceUrl, Object body, Map<String, String> headers){
        return this.postForObject(resourceUrl, body, headers, new HashMap<>());
    }

    @Override
    public <T> T postForObject(String resourceUrl, Object body, Map<String, String> headers, Class<T> tClass){
        return this.objectMapper.objectify(this.postForObject(resourceUrl, body, headers), tClass);
    }

    @Override
    public String postForObjectWithParams(String resourceUrl, Object body, Map<String, Object> params){
        return this.postForObject(resourceUrl, body, null, params);
    }

    @Override
    public <T> T postForObjectWithParams(String resourceUrl, Object body, Map<String, Object> params, Class<T> tClass){
        return this.objectMapper.objectify(this.postForObjectWithParams(resourceUrl, body, params), tClass);
    }

    @Override
    public String postForObject(String resourceUrl, Object body, Map<String, String> headers, Map<String, Object> params){
        String url = getDestinationResourcePath(resourceUrl);
        logger.logIrisApiRequest(url, HttpMethod.POST.name(), body, headers, params);
        String requestJson;
        if(body instanceof String){
            requestJson = (String) body;
        }else{
            requestJson = objectMapper.stringify(body);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, getHttpHeaders(headers));
        ResponseEntity<String> httpResponse;
        try {
            httpResponse = irisRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, getUriVariables(params));
            logger.logIrisApiResponse(httpResponse);
            return httpResponse.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException exception){
            String errorResponse = exception.getResponseBodyAsString();
            httpResponse = new ResponseEntity<>(errorResponse, exception.getStatusCode());
            logger.logIrisApiExceptionResponse(exception);
            return httpResponse.getBody();
        }
    }

    @SneakyThrows
    @Override
    public <T> IrisHttpResponse<T> postIrisEntity(String resourceUrl, Object requestBody, Map<String, String> headers, Map<String, Object> params, Class<T> tClass){
        String responseBody = this.postForObject(resourceUrl, requestBody, headers, params);
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject headerObject = jsonObject.getJSONObject("header");
        JSONObject bodyObject = jsonObject.getJSONObject("body");
        IrisHttpHeader header = objectMapper.objectify(headerObject.toString(), IrisHttpHeader.class);
        IrisHttpResponse<T> response = new IrisHttpResponse<>();
        if(tClass.isAssignableFrom(String.class)){
            response.setBody((T) responseBody);
            response.setHeader(header);
        }else{
            T body = objectMapper.objectify(bodyObject.toString(), tClass);
            response.setHeader(header);
            response.setBody(body);
        }
        return response;
    }

    @SneakyThrows
    @Override
    public <T> IrisHttpResponse<T> postIrisEntity(String resourceUrl, Object requestBody, Map<String, String> headers, Class<T> tClass){
        return this.postIrisEntity(resourceUrl, requestBody, headers, null, tClass);
    }

    private String getDestinationResourcePath(String resourcePath){
        String prefix = irisConnectionProperties.getProtocol()
                .concat(irisConnectionProperties.getProtocol().endsWith("://") ? "" : "://")
                .concat(irisConnectionProperties.getHost())
                .concat(":")
                .concat(String.valueOf(irisConnectionProperties.getPort()));
        if(resourcePath.startsWith(prefix))
            resourcePath = resourcePath.replace(prefix, "");
        if(resourcePath.startsWith("/")){
            return prefix.concat(resourcePath);
        }
        return prefix.concat("/").concat(resourcePath);
    }

    private HttpHeaders getHttpHeaders(Map<String, String> headers){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if(Optional.ofNullable(headers).isPresent()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.set(entry.getKey(), entry.getValue());
            }
        }
        return httpHeaders;
    }

    private Map<String, Object> getUriVariables(Map<String, Object> params){
        if(Optional.ofNullable(params).isPresent()){
            return params;
        }
        return new HashMap<>();
    }
}
