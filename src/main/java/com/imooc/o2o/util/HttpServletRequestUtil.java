package com.imooc.o2o.util;
/**
 * HttpServletRequest
 * 属性转型工具类
 */
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
    
	public static int getInt(HttpServletRequest request,String key) {
		try {
			return Integer.decode(request.getParameter(key));
		}catch(Exception e) {
			return -1;
		}
	}
	
	public static Long getLong(HttpServletRequest request,String key) {
		try {
			return Long.decode(request.getParameter(key));
		}catch(Exception e) {
			return -1L;
		}
	}
	
	public static double getDouble(HttpServletRequest request,String key) {
		try {
			return Double.valueOf(request.getParameter(key));
		}catch(Exception e) {
			return -1d;
		}
	}
	
	public static boolean getBoolean(HttpServletRequest request,String key) {
		try {
			return Boolean.valueOf(request.getParameter(key));
		}catch(Exception e) {
			return false;
		}
	}
	
	public static String getString(HttpServletRequest request, String key) {
		String result = request.getParameter(key);
		try {
			if(result != null) {
				result = result.trim();
			}
			if("".equals(result)) {
				return null;
			}
			return result;
		}catch(Exception e) {
			return null;
		}
	}
	
}
