package com.imooc.o2o.util;

public class PathUtil {
    
	//获取系统分隔符
	private static String separator = System.getProperty("file.separator");
	
	/**
	 * 获取不同系统环境下上传文件的存储地址
	 * @return String : the base path of image to save
	 */
	public static String getBasePathUtil() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if(os.toLowerCase().startsWith("win")) {
			basePath = "D:/projectdev/image";
		}else {
			basePath = "/Users/work/image";
		}
		basePath.replace("/", separator);
		return basePath;
	}
	
	/**
	 * 获取每个店铺的图片存储地址
	 * @param shopId 
	 * @return String 
	 */
	public static String getShopImagePath(long shopId) {
		String imagePath = "/upload/image/items/shop/" + shopId + "/";
		return imagePath.replace("/", separator);
	} 
	
//	public static void main(String[] args) {
//		String test = getBasePathUtil();
//		System.out.println(test);
//	}
}
