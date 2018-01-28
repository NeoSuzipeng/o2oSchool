package com.imooc.o2o.cache;
import java.util.Set;

/**
 * 封装Jedis多种数据结构的操作
 * @author su
 */
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

public class JedisUtil {
    //缓存默认存活时间
	private final static int EXPIRE = 6000;
	
	//Jedis连接池对象
	private JedisPool jedisPool;
	
	/** 操作Key的方法 */
	public Keys KEYS;
	/** 对存储结构为String类型的操作 */
	public Strings STRINGS;
    
	/**
	 * 获取Jedis连接池对象
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}
    
	/**
	 * 设置Jedis连接池对象
	 * @param jedisPoolWriper
	 */
	public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
		this.jedisPool = jedisPoolWriper.getJedisPool();
	}
	
	/**
	 * 获取Jedis对象
	 * @return
	 */
	public Jedis getJedis() {
		return jedisPool.getResource();
	}
	
	/**
	 * 设置Key的最大存活时间
	 * @param seconds
	 * @param key
	 */
	public void expire(int seconds , String key) {
		if(seconds <= 0) {
			return;
		}
		Jedis jedis = getJedis();
		jedis.expire(key, seconds);
		jedis.close();
	} 
	
	/**
	 * 设置Key的默认最大存活时间
	 * @param key
	 */
	public void expire(String key) {
		expire(EXPIRE, key);
	}
	
	public class Keys{
		
		/**
		 * 清空所有key值
		 * @return Status code reply
		 */
		public String flushAll() {
			Jedis jedis = getJedis();
			String data = jedis.flushAll();
			jedis.close();
			return data;
		}
		
		/**
		 * 更改key
		 * @param oldKey
		 * @param newKey
		 * @return operation status
		 */
		public String rename(String oldKey, String newKey) {
			Jedis jedis = getJedis();
			String status = jedis.rename(oldKey, newKey);
			jedis.close();
			return status;
		}
		
		/**
		 * 更改key,仅当新key不存在时才执行
		 * @param oldKey
		 * @param newKey
		 * @return 1 if the key was renamed 0 if the target key already exist
		 */
		public Long renamenx(String oldKey, String newKey) {
			Jedis jedis = getJedis();
			Long status = jedis.renamenx(oldKey, newKey);
			jedis.close();
			return status;
		}
		
		/**
		 * 设置key的过期时间，以秒为单位
		 * @param key
		 * @param seconds
		 * @return 1 the timeout was set; 0 the timeout not was set(1. the timeout has an assocation time ..)
		 */
		public Long expired(String key, int seconds) {
			Jedis jedis = getJedis();
			Long status = jedis.expire(key, seconds);
			jedis.close();
			return status;
		}
		
		/**
		 * 取消key的过期时间，以秒为单位
		 * @param key
		 * @param seconds
		 * @return 1 the timeout was set; 0 the timeout not was set(1. the timeout has an assocation time ..)
		 */
		public Long persist(String key) {
			Jedis jedis = getJedis();
			Long status = jedis.persist(key);
			jedis.close();
			return status;
		}
		/**
		 * 检验key是否存在
		 * @param key
		 * @return boolean
		 */
		public Boolean exist(String key) {
			Jedis jedis = getJedis();
			Boolean exit = jedis.exists(key);
			jedis.close();
			return exit;
		}
		
		/**
		 * 删除key值
		 * @param keys
		 * @return
		 */
		public Long del(String... keys) {
			Jedis jedis = getJedis();
			Long exit = jedis.del(keys);
			jedis.close();
			return exit;
		}
		
		/**
		 * 依据给定的正则匹配key
		 * @param keyPattern
		 * @return set
		 */
		public Set<String> keys(String keyPattern){
			Jedis jedis = getJedis();
			Set<String> set = jedis.keys(keyPattern);
			jedis.close();
			return set;
		}
	}
	
	public class Strings{
		
		/**
		 * 依据Key获取记录
		 * @param key
		 * @return key - value
		 */
		public String get(String key) {
			Jedis jedis = getJedis();
			String value = jedis.get(key);
			jedis.close();
			return value;
		}
		
		/**
		 * 设置Strings记录
		 * @param key
		 * @param value
		 * @return status
		 */
		public String set(String key, String value) {
			return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
		}
		
		/**
		 * 设置Strings记录
		 * @param key
		 * @param value
		 * @return status
		 */
		public String set(byte[] key, byte[] value) {
			Jedis jedis = getJedis();
			String stauts = jedis.set(key, value);
			jedis.close();
			return stauts;
		}
	}
}
