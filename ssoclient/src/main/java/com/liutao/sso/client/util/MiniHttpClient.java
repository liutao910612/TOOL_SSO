package com.liutao.sso.client.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MiniHttpClient {  
    private static Logger log = Logger.getLogger(MiniHttpClient.class);
      
    public static String post(String url, Map<String, String> params) {  
        //DefaultHttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String body = null;
        
        log.debug("QA_IP:" + url);
        HttpPost post = postForm(url, params);
          
        body = invoke(httpclient, post);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;
    }

    public static String postStringEntity(String url, String entity) {
        //DefaultHttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String body = null;

        log.debug("create httppost:" + url);
        HttpPost post = postEntity(url, entity);

        body = invoke(httpclient, post);

        httpclient.getConnectionManager().shutdown();

        return body;
    }

    public static String patchStringEntity(String url, String entity) {
        //DefaultHttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String body = null;

        log.debug("create httppost:" + url);
        HttpPatch patch = patchEntity(url, entity);

        body = invoke(httpclient, patch);

        httpclient.getConnectionManager().shutdown();

        return body;
    }


      
    public static String get(String url) {  
        //DefaultHttpClient httpclient = new DefaultHttpClient();
        DefaultHttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String body = null;  
          
        log.debug("create httppost:" + url);
        HttpGet get = new HttpGet(url);
        body = invoke(httpclient, get);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }

    public static String delete(String url) {
        DefaultHttpClient httpclient = null;
        try {
            httpclient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String body = null;

        log.debug("create httppost:" + url);
        HttpDelete delete = new HttpDelete(url);
        body = invoke(httpclient, delete);

        httpclient.getConnectionManager().shutdown();

        return body;
    }


    private static String invoke(DefaultHttpClient httpclient,
            HttpUriRequest httpost) {

        HttpResponse response = sendRequest(httpclient, httpost);

        if(response == null) {
            //如果没有返回,则连接超时
            return "timeout";
        }

        String body = paseResponse(response);  
          
        return body;
    }
  
    private static String paseResponse(HttpResponse response) {
        log.debug("get response from http server..");
        HttpEntity entity = response.getEntity();
          
        log.debug("response status: " + response.getStatusLine());
        String charset = EntityUtils.getContentCharSet(entity);
        log.debug(charset);
          
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);
            log.debug(body);
        } catch (ParseException e) {
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return body;  
    }  
  
    private static HttpResponse sendRequest(DefaultHttpClient httpclient,
                                            HttpUriRequest httpost) {
        log.debug("execute post...");
        HttpResponse response = null;
          
        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
            log.debug(" Connection refused...");
        }
        return response;  
    }  
  
    private static HttpPost postForm(String url, Map<String, String> params){
          
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();
          
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }  
          
        try {  
            log.debug("set utf-8 form entity to httppost");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  
        }  
          
        return httpost;  
    }

    private static HttpPost postEntity(String url, String entity){

        HttpPost httpost = new HttpPost(url);

        try {
            log.debug("set string entity to httppost");
            httpost.setEntity(new StringEntity(entity));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }

    private static HttpPatch patchEntity(String url, String entity){

        HttpPatch httpPatch = new HttpPatch(url);

        try {
            log.debug("set string entity to httppost");
            httpPatch.setEntity(new StringEntity(entity));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpPatch;
    }
}  
