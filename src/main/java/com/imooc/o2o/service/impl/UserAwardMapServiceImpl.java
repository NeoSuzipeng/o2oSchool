package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.UserAwardMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.exception.UserAwardMapOperationException;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService{
    
	@Autowired
	private UserAwardMapDao userAwardMapDao;
	
	@Autowired
	private UserShopMapDao userShopMapDao;
	
	/**
	 * 根据查询条件返回用户积分兑换的信息
	 */
	@Override
	public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex,
			Integer pageSize) {
		// 空值判断
	    if (userAwardCondition != null && pageIndex != null && pageSize != null) {
		    // 页转行
		    int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		    // 根据查询条件分页返回用户与奖品的映射信息列表(用户领取奖品的信息列表)
		    List<UserAwardMap> userAwardMapList = userAwardMapDao.selectUserAwardMapByCondition(userAwardCondition, rowIndex, pageSize);
		    
		    // 返回总数
		    int count = userAwardMapDao.selectUserAwardMapCountByCondition(userAwardCondition);
		    UserAwardMapExecution ue = new UserAwardMapExecution();
		    ue.setUserAwardMapList(userAwardMapList);
		    ue.setCount(count);
		    return ue;
	    } else {
			return null;
		}
	}

	@Override
	public UserAwardMap getUserAwardMapById(long userAwardMapId) {
		return userAwardMapDao.selectUserAwardMapById(userAwardMapId);
	}
    
	/**
	 * 用户领取奖品
	 */
	@Override
	@Transactional
	public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
		if(userAwardMap != null && userAwardMap.getAward() != null && userAwardMap.getAward().getAwardId() != null
				&& userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {
			userAwardMap.setCreateTime(new Date());
			//设置奖品可用状态    0.未兑换 1.以兑换
			userAwardMap.setUsedStatus(0);
			//判断是否需要消费积分
			if(userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
				try {
					//获取用户在该店铺下的积分
					UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),
							userAwardMap.getShop().getShopId());
					if(userShopMap != null) {
						//判断积分是否足够支付
						if(userShopMap.getPoint() >= userAwardMap.getPoint()) {
							//扣除积分
							userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
							int effectNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
							if(effectNum <= 0)
								throw new UserAwardMapOperationException("扣除积分失败");
						}else {
							return new UserAwardMapExecution(UserAwardMapStateEnum.FAIL);
						}
					}else {
						return new UserAwardMapExecution(UserAwardMapStateEnum.FAIL);
					}
					// 添加用户奖品领取信息
					int effectNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
					if(effectNum <= 0)
						throw new UserAwardMapOperationException("领取奖品失败");
					return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
				}catch (Exception e) {
					throw new UserAwardMapOperationException("内部错误");
				}
			}
		}
		return new UserAwardMapExecution(UserAwardMapStateEnum.FAIL);
	}
    
	/**
	 * 用户兑换奖品
	 */
	@Override
	public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
		// 空值判断，主要是检查传入的userAwardId以及领取状态是否为空
		if (userAwardMap == null || userAwardMap.getUserAwardId() == null || userAwardMap.getUsedStatus() == null) {
			return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_ERROR);
		} else {
			try {
				// 更新可用状态
				int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
				if (effectedNum <= 0) {
					return new UserAwardMapExecution(UserAwardMapStateEnum.FAIL);
				} else {
					return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
				}
			} catch (Exception e) {
				throw new UserAwardMapOperationException("modifyUserAwardMap error: " + e.getMessage());
			}
		}		
	}

}
