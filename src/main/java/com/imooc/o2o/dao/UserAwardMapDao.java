package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.UserAwardMap;

public interface UserAwardMapDao {
    
	/**
	 * 根据传入的条件查询用户奖品信息
	 * @return
	 */
	public List<UserAwardMap> selectUserAwardMapByCondition(@Param("userAwardCondition") UserAwardMap userAwardCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	
	/**
	 * 根据传入的条件查询用户奖品信息总数
	 * @param userAwardCondition
	 * @return
	 */
	public int selectUserAwardMapCountByCondition(@Param("userAwardCondition") UserAwardMap userAwardCondition);
	
	/**
	 * 根据传入的ID查询用户奖品信息
	 * @param userAwardMap
	 * @return
	 */
	public UserAwardMap selectUserAwardMapById(Long userAwardMap);
	
	/**
	 * 添加一条奖品兑换信息
	 * 
	 * @param userAwardMap
	 * @return
	 */
	int insertUserAwardMap(UserAwardMap userAwardMap);

	/**
	 * 更新奖品兑换信息，主要更新奖品领取状态
	 * 
	 * @param userAwardMap
	 * @return
	 */
	int updateUserAwardMap(UserAwardMap userAwardMap);
}
