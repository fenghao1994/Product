package com.singularityclub.shopping.Utils.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

/**
 * 发送Http请求的客户端
 * Created by fenghao on 2015/6/27.
 */
public class HttpClient {
    //后缀
    private static final String POSTFIX = ".json";
    //当前用户的认证token
    private static String _AUTH_TOKEN;
    private static final String CONTENT_TYPE_JSON = "application/json";

    private static AsyncHttpClient client = null;
    private static final int TIMEOUT = 60000 * 5;

    static{
        client = new AsyncHttpClient();
        client.setTimeout(TIMEOUT);
    }

    /**
     * 执行get请求
     * @param url 请求地址
     * @param params 请求参数
     * @param responseHandler 回调函数
     */
    public static void get(Context context, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(context, getAuthedUrl(getAbsoluteUrl(url)), params, responseHandler);
//		client.get(context, getAuthedUrl(getAbsoluteUrl(url)), getAuthHead(), params, responseHandler);
    }

    /**
     * 执行Post请求
     * @param url 请求地址
     * @param params 请求参数
     * @param responseHandler 回调函数
     */
    public static void post(Context context, String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        client.post(context, getAuthedUrl(getAbsoluteUrl(url)), getAuthHead(), getAuthedParams(params), null, responseHandler);
    }

    /**
     * 执行Put请求
     * @param url 请求地址
     * @param params 请求参数
     * @param responseHandler 回调函数
     */
    public static void put(Context context, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        if(params == null){
            params = new RequestParams();
        }
        params.put("_method", "PUT");
        client.post(context, getAuthedUrl(getAbsoluteUrl(url)), getAuthHead(), getAuthedParams(params), null, responseHandler);
    }

    /**
     * 执行Delete请求
     * @param url 请求地址
     * @param params 请求参数
     * @param responseHandler 回调函数
     */
    public static void delete(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        client.delete(context, getAuthedUrl(getAbsoluteUrl(url)), getAuthHead(), responseHandler);
    }

    /**
     * 执行Post请求
     * @param url 请求地址
     * @param Object json对象
     * @param responseHandler 回调函数
     */
    public static void post(Context context, String url, Object jsonObject,
                            AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity entity = new StringEntity(JacksonMapper.getMapper().writeValueAsString(jsonObject), HTTP.UTF_8);
            client.post(context, getAuthedUrl(getAbsoluteUrl(url)), getAuthHead(), entity, CONTENT_TYPE_JSON, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseHandler.onFinish();
        }
    }

    /**
     * 获取登录校验的Header
     * @return
     */
    public static Header[] getAuthHead(){
        return new Header[]{};
    }

    /**
     * 更换登录校验信息
     * @param token 登录使用的token
     */
    public static void resetAuth(String token){
        _AUTH_TOKEN = token;
    }

    /**
     * 添加根路径
     * @param relativeUrl 当前相当路径
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
//		return BASE_URL + relativeUrl + POSTFIX;
        return relativeUrl + POSTFIX;
    }

    /**
     * 添加校验信息
     * @param url
     * @return
     */
    public static String getAuthedUrl(String url){
        if(_AUTH_TOKEN == null){
            return url;
        }else if(url.endsWith("?")){
            return url + "&access_token=" + _AUTH_TOKEN;
        }else{
            return url + "?access_token=" + _AUTH_TOKEN;
        }
    }

    /**
     * 添加校验信息
     * @param params
     * @return
     */
    public static RequestParams getAuthedParams(RequestParams params){
        if(_AUTH_TOKEN == null){
            return params;
        }else{
            if(params == null){
                params = new RequestParams();
            }
            params.put("auth_token", _AUTH_TOKEN);
            return params;
        }
    }
}
