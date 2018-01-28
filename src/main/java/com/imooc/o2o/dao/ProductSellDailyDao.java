package com.imooc.o2o.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductSellDaily;

public interface ProductSellDailyDao {
    
	/**
	 * 根据条件查询商品的日销量
	 * 条件1：商品名模糊查询
	 * 条件2：店铺ID
	 * 条件3：时间段
	 * 条件4：大于开始时间
	 * 条件5：小于结束时间
	 * @param productSellDailyCondition
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<ProductSellDaily> selectProductSellDailyByCondition(@Param("productSellDailyCondition")ProductSellDaily productSellDailyCondition,
			                                      @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	/**
	 * 统计并插入商品销量
	 * @return
	 */
	public int insertProductSellDaily();
	
}
