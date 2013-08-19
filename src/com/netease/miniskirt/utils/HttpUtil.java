package com.netease.miniskirt.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Http访问工具类
 * @author Easer
 *
 */
public class HttpUtil {
	
	
	private static final String TAG = "Bingo-Test";

	//getPlan
	
	/**
	 * http post访问方式
	 * @param url 访问的url
	 * @param params 传递的键值对参数
	 * @return 访问结果
	 */
	public static String httpPost(String url, List<NameValuePair> params) {
		String strResult = "";
		/* 建立HTTP Post联机 */
		HttpPost httpRequest = new HttpPost(url);
		/*
		 * Post运作传送变量必须用NameValuePair[]数组储存
		 */
		/*List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));*/
		try {
			/* 发出HTTP request */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 取得HTTP response */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 取出响应字符串 */
				strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
//			return strResult;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage().toString();
		}
		return strResult;
	}
	
	/**
	 * http post访问方式
	 * @param url 访问的url
	 * @param params 传递的键值对参数
	 * @return 访问结果
	 */
	public static String httpPost(String url, List<NameValuePair> params, int successCode) {
		String strResult = "";
		/* 建立HTTP Post联机 */
		HttpPost httpRequest = new HttpPost(url);
		/*
		 * Post运作传送变量必须用NameValuePair[]数组储存
		 */
		/*List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));*/
		try {
			/* 发出HTTP request */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 取得HTTP response */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == successCode) {
				/* 取出响应字符串 */
				strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
//			return strResult;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage().toString();
		}
		return strResult;
	}
	
	/**
	 * http get方式访问
	 * @param url 访问的url
	 * @return 结果字符串
	 */
	public static String httpGet(String url) {
		String strResult = "";
		/* 建立HTTP Get联机 */
		HttpGet httpRequest = new HttpGet(url);
		try {
			/* 取得HTTP response */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 取出响应字符串 */
				strResult = EntityUtils.toString(httpResponse
						.getEntity());
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
//			return strResult;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage().toString();
		}
		return strResult;
	}
	
	/**
	 * MD5加密算法，用于对密码进行加密
	 * @param str
	 * @return
	 */
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
 
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
 
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
 
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    
}
