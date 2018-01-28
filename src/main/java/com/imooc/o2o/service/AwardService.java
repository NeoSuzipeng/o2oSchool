package com.imooc.o2o.service;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.exception.AwardOperationException;

public interface AwardService {
	/**
	 * 根据传入的条件分页返回奖品列表，并返回该查询条件下的总数
	 * 
	 * @param awardCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	AwardExecution getAwardList(Award awardCondition, Integer pageIndex, Integer pageSize);

	/**
	 * 根据awardId查询奖品信息
	 * 
	 * @param awardId
	 * @return
	 */
	Award getAwardById(Long awardId);

	/**
	 * 添加奖品信息，并添加奖品图片
	 * 
	 * @param award
	 * @param thumbnail
	 * @return
	 */
	AwardExecution addAward(Award award, ImageHolder thumbnail) throws AwardOperationException;

	/**
	 * 根据传入的奖品实例修改对应的奖品信息， 若传入图片则替换掉原先的图片
	 * 
	 * @param award
	 * @param thumbnail
	 * @param awardImgs
	 * @return
	 */
	AwardExecution modifyAward(Award award, ImageHolder thumbnail)  throws AwardOperationException ;
}
