package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.UserProductMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.exception.UserProductMapException;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class UserProductMapServiceImpl implements UserProductMapService{
	
	@Autowired
	private UserProductMapDao userProductMapDao;
	
	@Autowired
	private UserShopMapDao userShopMapDao;
    /**
     * 分页查询用户消费记录
     */
	@Override
	public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
			Integer pageSize) {
	    if(userProductCondition != null && pageIndex != null && pageSize != null) {
	    	//计算分页
	    	int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
	    	List<UserProductMap> userProductMaps = userProductMapDao.selectUserProductMapByCondition(userProductCondition, rowIndex, pageSize);
	    	UserProductMapExecution userProductMapExecution = new UserProductMapExecution();
	    	int count = 0;
	    	if(userProductMaps != null) {
	    		count = userProductMapDao.selectUserProductMapCountByCondition(userProductCondition);
	    	}
	    	userProductMapExecution.setCount(count);
    		userProductMapExecution.setUserProductMapList(userProductMaps);
    		return userProductMapExecution;
	    }
		return null;
	}
    
	/**
	 * 用户添加消费记录
	 */
	@Override
	@Transactional
	public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) {
		if(userProductMap != null && userProductMap.getUser().getUserId() != null && 
				   userProductMap.getShop().getShopId() != null && userProductMap.getOperator().getUserId() != null) {
			userProductMap.setCreateTime(new Date());
			try {
				//添加消费记录
				int effectNum = userProductMapDao.insertUserProductMap(userProductMap);
				if(effectNum <= 0)
					throw new UserProductMapException("添加消费记录失败");
				//如果购买商品可以得到积分
				if(userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
					//查看是否已经有积分信息
					// 查询该顾客是否在店铺消费过
					UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(),
							userProductMap.getShop().getShopId());
					if (userShopMap != null && userShopMap.getUserShopId() != null) {
						// 若之前消费过，即有过积分记录，则进行总积分的更新操作
						userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());
						effectNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
						if (effectNum <= 0) {
							throw new UserProductMapException("添加积分失败");
						}
					}else {
						// 在店铺没有过消费记录，添加一条店铺积分信息(就跟初始化会员一样)
						userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(),
								userProductMap.getShop().getShopId(), userProductMap.getPoint());
						effectNum = userShopMapDao.insertUserShopMap(userShopMap);
						if (effectNum <= 0) {
							throw new UserProductMapException("创建积分失败");
						}
				} 
					return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
				}
			}catch (Exception e) {
				throw new UserProductMapException("添加消费记录失败 :" + e.toString());
			}
		}
		return new UserProductMapExecution(UserProductMapStateEnum.NULL_ERROR);
	}
	
	/**
	 * 封装顾客积分信息
	 * 
	 * @param userId
	 * @param shopId
	 * @param point
	 * @return
	 */
	private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {
		UserShopMap userShopMap = null;
		// 空值判断
		if (userId != null && shopId != null) {
			userShopMap = new UserShopMap();
			PersonInfo customer = new PersonInfo();
			customer.setUserId(userId);
			Shop shop = new Shop();
			shop.setShopId(shopId);
			userShopMap.setUser(customer);
			userShopMap.setShop(shop);
			userShopMap.setCreateTime(new Date());
			userShopMap.setPoint(point);
		}
		return userShopMap;
	}
	
}
