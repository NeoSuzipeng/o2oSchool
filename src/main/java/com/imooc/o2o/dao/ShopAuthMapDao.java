package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ShopAuthMap;

public interface ShopAuthMapDao {
	/**
	 * 分页列出店铺下面的授权信息
	 * 
	 * @param shopId
	 * @param beginIndex
	 * @param pageSize
	 * @return
	 */
	List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") long shopId, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * 获取授权总数
	 * 
	 * @param shopId
	 * @return
	 */
	int queryShopAuthCountByShopId(@Param("shopId") long shopId);

	/**
	 * 新增一条店铺与店员的授权关系
	 * 
	 * @param shopAuthMap
	 * @return effectedNum
	 */
	int insertShopAuthMap(ShopAuthMap shopAuthMap);

	/**
	 * 更新授权信息
	 * 
	 * @param shopAuthMap
	 * @return
	 */
	int updateShopAuthMap(ShopAuthMap shopAuthMap);

	/**
	 * 对某员工除权
	 * 
	 * @param employeeId
	 * @param shopId
	 * @return effectedNum
	 */
	int deleteShopAuthMap(long shopAuthId);

	/**
	 * 通过shopAuthId查询员工授权信息
	 * 
	 * @param shopAuthId
	 * @return
	 */
	ShopAuthMap queryShopAuthMapById(Long shopAuthId);
}
