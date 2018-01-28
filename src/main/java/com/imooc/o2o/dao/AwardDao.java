package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Award;

public interface AwardDao {
    
	/**
	 * 通过条件查找奖品
	 * @param shopId
	 * @return
	 */
	public List<Award> selectAwardByCondition(@Param("awardCondition") Award awardCondition, @Param("rowIndex")int rowIndex,
			                                                  @Param("pageSize")int pageSize);
	
	/**
	 * 通过条件查找奖品总数
	 * @param awardCondition
	 * @return
	 */
	public int selectAwardCountByCondition(@Param("awardCondition") Award awardCondition);
	
	/**
	 * 通过奖品ID查找奖品
	 * @param awardId
	 * @return
	 */
	public Award selectAwardByAwardId(long awardId);
	
	/**
	 * 通过奖品ID更新奖品（主要是更新奖品状态，积分）
	 * @param award
	 * @return
	 */
	public int updateAwardByAwardId(Award award);
	
	/**
	 * 插入奖品
	 * @param award
	 * @return
	 */
	public int insertAward(Award award);
	
	/**
	 * 删除奖品
	 * @param award
	 * @return
	 */
	public int deleteAward(@Param("awardId")long awardId,@Param("shopId") long shopId);
	
}
