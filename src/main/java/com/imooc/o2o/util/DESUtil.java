package com.imooc.o2o.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 实现DES对称加密
 * 即解密与加密使用同一个密匙
 * @author 10353
 *
 */
public class DESUtil {
    //保存密匙的对象
	private static Key key;
	
	//密匙种子
	private final static String KEY_STR = "su";
	private final static String CHARACTER = "UTF-8";
	private final static String ALGORITHM = "DES";
	
	//类加载时DES生成算法对象
	static {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			//生成SAH1安全策略实例
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			//设置密匙种子
			secureRandom.setSeed(KEY_STR.getBytes());
			//基于SAH1加密策略生成加密算法对象
			generator.init(secureRandom);
			//生成密匙
			key = generator.generateKey();
			generator = null;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 获取加密后的属性值
	 * @param propertyValue
	 * @return
	 */
	public static String getEncropyString(String propertyValue) {
		BASE64Encoder base64encoder = new BASE64Encoder();
		//加密算法需要用到byte数组
		try {
			byte[] bytes = propertyValue.getBytes(CHARACTER);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			//init参数解释：1.opomode 选择解密操作或者加密操作哦 2.密匙
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] doFinal = cipher.doFinal(bytes);
			return base64encoder.encode(doFinal);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
    
	/**
	 * 获取加密后的属性值
	 * @param propertyValue
	 * @return
	 */
	public static String getDecryptString(String propertyValue) {
		BASE64Decoder base64Decoder = new BASE64Decoder();
		try {
			byte[] bytes = base64Decoder.decodeBuffer(propertyValue);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			//init参数解释：1.opomode 选择解密操作或者加密操作哦 2.密匙
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] doFinal = cipher.doFinal(bytes);
			return new String(doFinal, CHARACTER);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		String encropyStr = getEncropyString("szp9986");
		String decryptStr = getDecryptString("+C5SvPdcQ3vNzxilqSfY4g==");
		System.out.println(encropyStr);
		System.out.println(decryptStr);
	}
}
