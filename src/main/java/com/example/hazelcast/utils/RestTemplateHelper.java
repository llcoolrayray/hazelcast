package com.example.hazelcast.utils;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 * @author y97637
 * @date 2020/9/15
 */
@Component
public class RestTemplateHelper {


    private static final String APPLICATION_STREAM = "application/octet-stream";
    private static final int REDIRECT = 301;

    @Autowired
    private RestTemplate restTemplate;

    public <T> T post(String url, Object request, Class<T> responseType, Map<String, Object> params, String token) {
        Map<String, String> headers = new HashMap<>(1);
        headers.put("x-access-token", token);
        return exchange(buildUrl(url, params), HttpMethod.POST, request, responseType, params, headers);
    }

    /**
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     * 登录
     */
    public <T> T post(String url, Object request, Class<T> responseType) {
        return exchange(buildUrl(url, null), HttpMethod.POST, request, responseType, null, null);
    }

    private <T> T exchange(String url,
                           HttpMethod method,
                           Object request,
                           Class<T> responseType,
                           Map<String, Object> params,
                           Map<String, String> headerMap) {
        ResponseEntity<T> resultEntity = null;
        try {
            // 请求头
            HttpHeaders headers = getDefaultHeaders();

            if (headerMap != null && !CollectionUtils.isEmpty(headerMap)) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    headers.set(entry.getKey(), entry.getValue());
                }
            }

            //提供json转化功能
            String json = request == null ? null : JSONObject.toJSONString(request);

            // 发送请求
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            if (params == null || params.size() == 0) {
                resultEntity = restTemplate.exchange(url, method, entity, responseType);
            } else {
                resultEntity = restTemplate.exchange(url, method, entity, responseType, params);
            }

            return resultEntity.getBody();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("JSON.toJSONString failed,errmessage12312=" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("JSON.toJSONString failed,errmessage=" + e.getMessage());
        }
    }



    public String buildUrl(String url, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if (params == null || params.size() == 0) {
            return builder.toString();
        }
        builder.append("?");
        for (String key : params.keySet()) {
            builder.append(key).append("=");
            builder.append("{").append(key).append("}").append("&");
        }
        return builder.toString().substring(0, builder.toString().length() - 1);
    }



    public static String assemblyUrl(String https, String address, int port) {
        return https + "://" + address + ":" + port + "/";
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
        headers.setContentType(mediaType);

        return headers;
    }


}
