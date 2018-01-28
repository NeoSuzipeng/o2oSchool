package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exception.ShopOperationException;

public interface ShopService {
	
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 * 
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
	
	
	/**
	 * 通过店铺Id获取店铺信息
	 * @param shopId
	 * @return
	 */
	public Shop getShopById(long shopId);
	
	/**
	 * 更新店铺信息
	 * 包括对店铺图片的修改
	 * @param shop
	 * @param shopImg
	 * @param shopImgName
	 * @return ShopExecution
	 */
	public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder)
	       throws ShopOperationException;
	/**
	 * 注册店铺
	 * 添加店铺信息包括图片
	 * @param shop
	 * @param shopImg
	 * @param shopImgName
	 * @return
	 */
    public ShopExecution addShop(Shop shop, ImageHolder imageHolder)
    		throws ShopOperationException;
}
