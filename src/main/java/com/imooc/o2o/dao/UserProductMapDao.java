package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.UserProductMap;

public interface UserProductMapDao {
    
	/**
	 * 根据条件查询用户消费记录
	 * 条件1.用户发起查询通过用户的ID
	 * 条件2.店家发起查询通过店铺ID
	 * @param userProductCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	public List<UserProductMap> selectUserProductMapByCondition(@Param("userProductCondition")UserProductMap userProductCondition,
			                                    @Param("rowIndex")Integer rowIndex, @Param("pageSize")Integer pageSize);
	
	/**
	 * 根据条件查询用户消费记录总数用于分页
	 * @param userProductCondition
	 * @return
	 */
	public int selectUserProductMapCountByCondition(@Param("userProductCondition")UserProductMap userProductCondition);
	
	/**
	 * 插入用户消费记录
	 * @param userProductMap
	 * @return
	 */
	public int insertUserProductMap(UserProductMap userProductMap);
}
