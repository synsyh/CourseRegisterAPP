package com.example.myapplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class sendPostRequest {

    static long responseLength = 0; // 响应长度// 响应长度
    static String responseContent = null; // 响应内容
    static HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例

    @SuppressWarnings("finally")
    public static String getRes(String requestUrl, String Username, String Password, String str) {
        httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(requestUrl); // 创建HttpPost
        List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 创建参数队列
        formParams.add(new BasicNameValuePair("grant_type", "password"));
        formParams.add(new BasicNameValuePair("username", Username));
        formParams.add(new BasicNameValuePair("password", Password + "|" + str + "*" + MainActivity.TempGuid));
        formParams.add(new BasicNameValuePair("client_id", "ynumisSite"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(sendPostRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost); // 执行POST请求
        } catch (IOException ex) {
            Logger.getLogger(sendPostRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpEntity entity = response.getEntity(); // 获取响应实体
        if (null != entity) {
            responseLength = entity.getContentLength();
            try {
                responseContent = EntityUtils.toString(entity, "UTF-8");
                // EntityUtils.consume(entity); //Consume response content
            } catch (IOException ex) {
                Logger.getLogger(sendPostRequest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(sendPostRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // System.out.println("请求地址: " + httpPost.getURI());
        // System.out.println("响应状态: " + response.getStatusLine());
        // System.out.println("响应长度: " + responseLength);
        // System.out.println("响应内容: " + responseContent);

        httpClient.getConnectionManager().shutdown(); // 关闭连接，释放资源
        return responseContent;
    }
}
