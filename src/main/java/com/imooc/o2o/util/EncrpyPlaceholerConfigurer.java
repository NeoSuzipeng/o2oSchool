package com.imooc.o2o.util;
/**
 * 定制property文件加载方式
 */
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncrpyPlaceholerConfigurer extends PropertyPlaceholderConfigurer{
	
    private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};
    
    /**
     * 重写父类PropertyResourceConfigurer方法
     * @param propertyName Spring从properties文件中加载的属性名
     * @param propertyValue Spring从properties文件中加载的属性值
     * @return propertyValue 解密后的属性值
     */
	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if(isEncropyName(propertyName)) 
			return DESUtil.getDecryptString(propertyValue);
		return propertyValue;
	}

	private boolean isEncropyName(String propertyName) {
		boolean flag = false;
		for(String propertedValue: encryptPropNames) {
			if(propertyName.equals(propertedValue)) {
				flag =true;
			}
		}
		return flag;
	}
	
	
}
