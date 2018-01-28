package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.UserProductMap;

/**
 * 用户消费记录
 * @author 10353
 *
 */
public interface UserProductMapService {
	/**
	 * 通过传入的查询条件分页列出用户消费信息列表
	 * 
	 * @param shopId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
			Integer pageSize);
	
	/**
	 * 用户添加商品消费记录
	 * @param userProductMap
	 * @return
	 */
	UserProductMapExecution addUserProductMap(UserProductMap userProductMap);
}
