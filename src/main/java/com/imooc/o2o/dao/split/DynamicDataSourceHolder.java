package com.imooc.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    
    //保证线程安全
    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    
    public static final String DB_MASTER = "master";
    
    public static final String DB_SLAVE="slave";
    
    /**
     * 获取线程的 dbType
     * @return null
     */
    public static String getDbType() {
    	String dbType = contextHolder.get();
    	if(null == dbType) {
    		dbType = DB_MASTER;  //默认数据源
    	}
    	return dbType;
    }
    
    /**
     * 设置线程的dbType
     * @param dbType
     */
    public static void setDbType(String dbType) {
    	logger.debug("设置线程数据源： "+ dbType);
    	contextHolder.set(dbType);
    }
}
