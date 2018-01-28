package com.imooc.o2o.service.impl;
import java.io.IOException;
import java.util.ArrayList;
/**
 * 区域信息
 * 引入Redis缓存技术
 */
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.exception.AreaOperationException;
import com.imooc.o2o.service.AreaService;

@Service
public class AreaSeviceImpl implements AreaService{
    
	private Logger logger = LoggerFactory.getLogger(AreaSeviceImpl.class);
	
	@Autowired
	private AreaDao dao;
	
	@Autowired
	private JedisUtil.Keys keys;
	
	@Autowired
	private JedisUtil.Strings strings;
	
	private final static String AREA_LIST_KEY = "areaList";
	/**
	 * 1.从jedis中取出数据
	 * 2.如果jedis中不存在则从数据库中查询
	 * 3.将数据库中获取的数据存入jedis
	 */
	@Override
	@Transactional
	public List<Area> getQueryArea() {
		String key = AREA_LIST_KEY;
		List<Area> areaList = null;
		ObjectMapper objectMapper = new ObjectMapper();
		if(!keys.exist(key)) {
			areaList = dao.queryArea();
			String jsonStr;
			try {
				jsonStr = objectMapper.writeValueAsString(areaList);
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
			strings.set(key, jsonStr);
			logger.debug("using redis cache");
		}else {
			String jsonString = strings.get(key);
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
			try {
				areaList = objectMapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		}
		return areaList;
	}

}
