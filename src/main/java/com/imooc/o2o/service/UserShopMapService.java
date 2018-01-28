package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;

public interface UserShopMapService {

	/**
	 * 查询指定店铺下的用户积分
	 * @param userShopMapCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, Integer pageIndex, Integer pageSize);
	
	/**
	 * 查询用户在指定店铺下的积分
	 * @return UserShopMapExecution
	 */
	public UserShopMap getUserShopMap(Long userId, Long shopId);
}
