package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class UserShopMapServiceImpl implements UserShopMapService{
    
	@Autowired
	private UserShopMapDao userShopMapDao;
    
	/**
	 * 获取指定条件下的用户积分信息列表
	 * 作用域：1.店铺管理 2.前端用户查询自身所有积分
	 */
	@Override
	public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, Integer pageIndex, Integer pageSize) {
		 if(userShopMapCondition != null || pageIndex != null || pageSize != null) {
				// 页转行
		        int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			    // 根据传入的查询条件分页返回用户积分列表信息
			    List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition, beginIndex,
									pageSize);
				// 返回总数
			    int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);
				UserShopMapExecution ue = new UserShopMapExecution();
				ue.setUserShopMapList(userShopMapList);
				ue.setCount(count);
			    return ue;
			}
			return null;
	}
	
	/**
	 * 查询用户在指定店铺下的积分
	 * @return UserShopMapExecution
	 */
	@Override
	public UserShopMap getUserShopMap(Long userId, Long shopId) {
		return userShopMapDao.queryUserShopMap(userId, shopId);
	}
	

}
