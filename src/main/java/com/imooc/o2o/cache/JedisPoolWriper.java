package com.imooc.o2o.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolWriper {
      
	private Logger logger = LoggerFactory.getLogger(JedisPoolWriper.class);
	   
	  //Jedis连接池对象
	private JedisPool jedisPool;

	public JedisPoolWriper(final JedisPoolConfig jedisPoolConfig,final String hostName,final int port) {
		try {
		    jedisPool = new JedisPool(jedisPoolConfig, hostName, port);
		}catch (Exception e) {
			logger.debug("init jedispool fail");
			e.printStackTrace();
		}
		logger.info("init jedispool success");
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	  
	
}
