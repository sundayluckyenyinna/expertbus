package com.accionmfb.expertbus.service;

import com.accionmfb.expertbus.payload.response.IrisHttpResponse;
import lombok.SneakyThrows;

import java.util.Map;

public interface IrisConnectionTemplate {
    String getForObject(String resourceUrl);

    <T> T getForObject(String resourceUrl, Class<T> tClass);

    String getForObject(String resourceUrl, Map<String, String> headers);

    <T> T getForObject(String resourceUrl, Map<String, String> headers, Class<T> tClass);

    String getForObjectWithParams(String resourceUrl, Map<String, Object> params);

    <T> T getForObjectWithParams(String resourceUrl, Map<String, Object> params, Class<T> tClass);

    String getForObject(String resourceUrl, Map<String, String> headers, Map<String, Object> params);

    @SneakyThrows
    <T> IrisHttpResponse<T> getIrisEntity(String resourceUrl, Map<String, String> headers, Map<String, Object> params, Class<T> tClass);

    <T> IrisHttpResponse<T> getIrisEntity(String resourceUrl, Map<String, String> headers, Class<T> tClass);

    String postForObject(String resourceUrl, Object body);

    <T> T postForObject(String resourceUrl, Object body, Class<T> tClass);

    String postForObject(String resourceUrlrl, Object body, Map<String, String> headers);

    <T> T postForObject(String resourceUrl, Object body, Map<String, String> headers, Class<T> tClass);

    String postForObjectWithParams(String resourceUrl, Object body, Map<String, Object> params);

    <T> T postForObjectWithParams(String resourceUrl, Object body, Map<String, Object> params, Class<T> tClass);

    String postForObject(String resourceUrl, Object body, Map<String, String> headers, Map<String, Object> params);

    @SneakyThrows
    <T> IrisHttpResponse<T> postIrisEntity(String resourceUrl, Object body, Map<String, String> headers, Map<String, Object> params, Class<T> tClass);

    @SneakyThrows
    <T> IrisHttpResponse<T> postIrisEntity(String resourceUrl, Object requestBody, Map<String, String> headers, Class<T> tClass);
}
