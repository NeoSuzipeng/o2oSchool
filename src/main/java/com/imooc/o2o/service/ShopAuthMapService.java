package com.imooc.o2o.service;
/**
 * @author 10353
 *
 */

import com.imooc.o2o.dto.ShopAuthMapExection;
import com.imooc.o2o.entity.ShopAuthMap;

public interface ShopAuthMapService {
    
	/**
	 * 查询店铺店员
	 * @return
	 */
	public ShopAuthMapExection listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);
	
	/**
	 * 根据员工号查找员工信息
	 * @param shopAuthId
	 * @return
	 */
	public ShopAuthMap getShopAuthMapById(Long shopAuthId);
	
	
	/**
	 * 添加店铺店员
	 * @param shopId
	 * @param userId
	 * @return
	 */
	public ShopAuthMapExection addShopAuthMap(ShopAuthMap shopAuthMap);
	
	/**
	 * 更新店铺店员信息
	 * 1.更新职位
	 * 2.更新状态
	 * @param shopAuthMap
	 * @return
	 */
	public ShopAuthMapExection modifyShopAuthMap(ShopAuthMap shopAuthMap);
	
}
