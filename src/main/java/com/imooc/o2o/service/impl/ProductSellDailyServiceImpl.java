package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.dao.ProductSellDailyDao;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.service.ProductSellDailyService;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService{
    
	private static final Logger log = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
	
	@Autowired
	private ProductSellDailyDao productSellDailyDao;
	
	/**
	 * 每日定时对所有店铺的商品的商品的销量进行统计
	 */
	@Override
	public void dailyCalculate() {
		log.info("Quartz Running Time : " + new Date());
		productSellDailyDao.insertProductSellDaily();
	}
    
	/**
	 * 根据查询条件返回店铺商品销量情况
	 */
	@Override
	public List<ProductSellDaily> listProductSellDaiy(ProductSellDaily productSellDailyCondition, Date beginTime,
			Date endTime) {
		return productSellDailyDao.selectProductSellDailyByCondition(productSellDailyCondition, beginTime, endTime);
	}

}
