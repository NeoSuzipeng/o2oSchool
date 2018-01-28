package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 新增店铺
	 * @param shop
	 * @return int : Insert the number of impact 
	 */
    public int insertShop(Shop shop);
    
    /**
     * 更新店铺
     * @param shop
     * @return  int : Update the number of success 
     */
    public int updateShop(Shop shop);
    
    /**
     * 通过店铺Id查询店铺信息
     * @param shopId
     * @return 对应店铺的店铺信息
     */
    public Shop queryShopById(Long shopId);
    
    /**
	 * 查询一定条件下店铺数量
	 * @param shopCondition
	 * @return 店铺数量
	 */
	public int queryShopCount(@Param("shopCondition") Shop shopCondition);
	
	/**
	 * 通过条件模糊查询店铺列表信息
	 * 条件有：店铺名 店铺类别(包含上级类别) 区域 状态 拥有者
	 * @param shopCondition
	 * @param rowindex
	 * @param pageSize
	 * @return 店铺列表信息
	 */
	public List<Shop> quertShopList(@Param("shopCondition")Shop shopCondition, @Param("rowIndex")int rowIndex,
			@Param("pageSize")int pageSize);
}
