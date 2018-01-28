package com.imooc.o2o.service;

import java.util.Date;
import java.util.List;

import com.imooc.o2o.entity.ProductSellDaily;

public interface ProductSellDailyService {
    
	/**
	 * 每日定时对所有店铺的商品的商品的销量进行统计
	 */
	public void dailyCalculate();
	
	
	/**
	 * 根据查询条件返回店铺商品销量情况
	 * @param productSellDailyCondition
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<ProductSellDaily> listProductSellDaiy(ProductSellDaily productSellDailyCondition,
			               Date beginTime, Date endTime);
}
