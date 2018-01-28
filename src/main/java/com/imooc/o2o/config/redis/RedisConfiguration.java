package com.imooc.o2o.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imooc.o2o.cache.JedisPoolWriper;
import com.imooc.o2o.cache.JedisUtil;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
	private String hostName;
    
	@Value("${redis.port}")
	private int port;
	
	@Value("${redis.database}")
	private int database;
	
	@Value("${redis.pool.maxActive}")
	private int maxActive;
	
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	
	@Value("${redis.pool.maxWait}")
	private long maxWait;
	
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	
	@Autowired
	private JedisPoolWriper jedisWritePool;
	
	@Autowired
	private JedisUtil jedisUtil;
	
	/**
	 * 设置Redis连接池相关配置
	 * @return
	 */
	@Bean(name="jedisPoolConfig")
	public JedisPoolConfig createJedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxActive);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
	}
	
	/**
	 * 创建Redis连接池
	 * @return
	 */
	@Bean(name="jedisPoolWriper")
	public JedisPoolWriper createJedisPoolWriper() {
		JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostName, port);
		return jedisPoolWriper;
	}
	
	/**
	 * 创建Redis工具类，封装好Redis的连接以进行相关的操作
	 * 
	 * @return
	 */
	@Bean(name = "jedisUtil")
	public JedisUtil createJedisUtil() {
		JedisUtil jedisUtil = new JedisUtil();
		jedisUtil.setJedisPool(jedisWritePool);
		return jedisUtil;
	}
	
	/**
	 * Redis的key操作
	 * @return
	 */
	@Bean(name="jedisKeys")
	public JedisUtil.Keys createJedisUtilKeys(){
		return jedisUtil.new Keys();
	}
	
	/**
	 * Redis的String操作
	 * @return
	 */
	@Bean(name="jedisString")
	public JedisUtil.Strings createJedisUtilStrings(){
		return jedisUtil.new Strings();
	}
}
