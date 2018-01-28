package com.imooc.o2o.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 通过百度短视频获取一个短的URL
 * @author 10353
 */
public class ShortNetAddressUtil {
	private static Logger logger = LoggerFactory.getLogger(ShortNetAddressUtil.class);
	
	private static int TIMEOUT = 30 * 1000;
	private static String ENCODING = "UTF-8";
	
	public static String getShortURL(String originURL) {
		String tinyURL = null;
		try {
			URL baiduUrl = new URL("http://dwz.cn/create.php");
			HttpURLConnection connection = (HttpURLConnection)baiduUrl.openConnection();
			logger.info("ready create connection...");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(TIMEOUT);
			connection.setRequestMethod("POST");
			String postData = URLEncoder.encode(originURL, "utf-8");
			// 输出原始的url
			connection.getOutputStream().write(("url=" + postData).getBytes());
		    // 连接百度短视频接口
			connection.connect();
			// 获取返回的字符串
			String responseStr = getResponseStr(connection);
			logger.info("response string: " + responseStr);
			// 在字符串里获取tinyurl，即短链接
			tinyURL = getValueByKey(responseStr, "tinyurl");
			logger.info("tinyurl: " + tinyURL);
			// 关闭链接
			connection.disconnect();
		}catch (Exception e) {
			logger.error("url error :" + e.toString());
		}
		return tinyURL;
	}
    
	/**
	 * 根据传入的key获取JSON对象中的值
	 * @param replyText
	 * @param key
	 * @return
	 */
	private static String getValueByKey(String replyText, String key) {
		ObjectMapper mapper = new ObjectMapper();
		// 定义json节点
		JsonNode node;
		String targetValue = null;
		try {
			// 把调用返回的消息串转换成json对象
			node = mapper.readTree(replyText);
			// 依据key从json对象里获取对应的值
			targetValue = node.get(key).asText();
		} catch (JsonProcessingException e) {
			logger.error("getValueByKey error:" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("getValueByKey error:" + e.toString());
		}

		return targetValue;
	}

	private static String getResponseStr(HttpURLConnection connection) throws IOException {
		StringBuffer result = new StringBuffer();
		// 从连接中获取http状态码
		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			// 如果返回的状态码是OK的，那么取出连接的输入流
			logger.debug("create connection success");
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
			String inputLine = "";
			while ((inputLine = reader.readLine()) != null) {
				// 将消息逐行读入结果中
				result.append(inputLine);
			}
		}
		// 将结果转换成String并返回
		return String.valueOf(result);
	}
	public static void main(String[] args) {
		getShortURL("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0ef11c0049518bbb&redirect_uri=http://smyang.s1.natapp.cc/o2o/shopadmin/addshopauthmap&response_type=code&scope=snsapi_userinfo&state=%7BaaashopIdaaa%3A29%2CaaacreateTimeaaa%3A1515673525341%7D#wechat_redirect");
	}
}
