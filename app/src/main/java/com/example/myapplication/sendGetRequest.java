package com.example.myapplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.*;
import java.io.IOException;

public class sendGetRequest {

    long responseLength = 0; // 响应长度// 响应长度
    String responseContent = null; // 响应内容
    HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例

    public sendGetRequest(String requestUrl, String Authorization, String access_token) {
        HttpGet httpGet = new HttpGet(requestUrl); // 创建HttpGet
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(Authorization, access_token);
        httpGet.addHeader(Authorization, access_token);
        try {
            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (null != entity) {
                responseLength = entity.getContentLength();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                // EntityUtils.consume(entity); //Consume response content
            }
            //System.out.println("请求地址: " + httpGet.getURI());
            //System.out.println("响应状态: " + response.getStatusLine());
            //System.out.println("响应长度: " + responseLength);
            //System.out.println("响应内容: " + responseContent);
        } catch (ClientProtocolException e) {
            // 该异常通常是协议错误导致
            // 比如构造HttpGet对象时传入的协议不对(将"http"写成"htp")或者服务器端返回的内容不符合HTTP协议要求等
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // 该异常通常是网络原因引起的,如HTTP服务器未启动等
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown(); // 关闭连接，释放资源
        }
    }

    public String getResponseContent() {
        return responseContent;
    }
}
