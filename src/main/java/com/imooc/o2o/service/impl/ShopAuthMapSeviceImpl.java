package com.imooc.o2o.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dto.ShopAuthMapExection;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapEnum;
import com.imooc.o2o.exception.ShopAuthMapException;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class ShopAuthMapSeviceImpl implements ShopAuthMapService{
    
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;
	
	@Override
	public ShopAuthMapExection listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
		if(shopId != null && pageIndex != null && pageSize != null) {
			//计算数据索引
			Integer rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<ShopAuthMap> shopAuthMaps = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, rowIndex, pageSize);
			ShopAuthMapExection shopAuthMapExection = new ShopAuthMapExection();
			int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
			shopAuthMapExection.setCount(count);
			shopAuthMapExection.setShopAuthMapList(shopAuthMaps);
			shopAuthMapExection.setState(ShopAuthMapEnum.SUCCESS.getState());
			return shopAuthMapExection;
		}
		return null;
	}
    
	/**
	 * 获取某店铺下的某店员信息
	 * 作用：编辑店员时提供信息
	 */
	@Override
	public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
		return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
	}

	@Override
	@Transactional
	public ShopAuthMapExection addShopAuthMap(ShopAuthMap shopAuthMap) {
		if(shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null
				&& shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
			shopAuthMap.setCreateTime(new Date());
			shopAuthMap.setLastEditTime(new Date());
			shopAuthMap.setEnableStatus(1);
			shopAuthMap.setTitleFlag(0);
			try {
				//插入数据
				int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
				if(effectNum <= 0)
					throw new ShopAuthMapException("授权失败");
			    return new ShopAuthMapExection(ShopAuthMapEnum.SUCCESS, shopAuthMap);
			}catch (Exception e) {
				throw new ShopAuthMapException("添加授权失败");
			}
		}
		return new ShopAuthMapExection(ShopAuthMapEnum.NULL_AUTH);
	}

	@Override
	@Transactional
	public ShopAuthMapExection modifyShopAuthMap(ShopAuthMap shopAuthMap) {
		if(shopAuthMap == null || shopAuthMap.getShopAuthId() == null)
			return new ShopAuthMapExection(ShopAuthMapEnum.NULL_AUTH);
		else {
			try {
				int effectNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
				if(effectNum <= 0)
					return new ShopAuthMapExection(ShopAuthMapEnum.FAIL);
				else
					return new ShopAuthMapExection(ShopAuthMapEnum.SUCCESS, shopAuthMap);
			}catch (Exception e) {
				throw new ShopAuthMapException("修改授权失败");
			}
		}
	}
    
}
