package com.geekbang.camp;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientDemo {

    public static void main(String[] args) {
        //设置请求配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(200)
                .setConnectionRequestTimeout(200)
                .setSocketTimeout(200)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet httpGet = new HttpGet("http://localhost:8801");
        httpGet.setConfig(requestConfig);
        try {
            //发起请求
            CloseableHttpResponse reponse = httpClient.execute(httpGet);
            //输出服务端响应返回信息
            System.out.println("http reponse code:" + reponse.getStatusLine().getStatusCode());
            HttpEntity httpEntity = reponse.getEntity();
            String content = "";
            if(httpEntity !=null) {
                content = EntityUtils.toString(httpEntity, "UTF-8");
            }
            System.out.println("http reponse content:" + content);
        } catch (IOException e) {
            System.out.println("http request exception:" + e.getMessage());
        }
    }

}
