package com.imooc.o2o.service.impl;

import static org.mockito.Matchers.anyBoolean;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.AwardDao;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.exception.AwardOperationException;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class AwardServiceImpl implements AwardService{
    
	@Autowired
	private AwardDao awardDao;
	
	@Override
	public AwardExecution getAwardList(Award awardCondition, Integer pageIndex, Integer pageSize) {
		// 空值判断
	    if (awardCondition != null && pageIndex != null && pageSize != null) {
		    // 页转行
		    int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		    // 根据查询条件分页返回用户与奖品的映射信息列表(用户领取奖品的信息列表)
		    List<Award> awards = awardDao.selectAwardByCondition(awardCondition, rowIndex, pageSize);
		    
		    // 返回总数
		    int count = awardDao.selectAwardCountByCondition(awardCondition);
		    AwardExecution ue = new AwardExecution();
		    ue.setAwardList(awards);
		    ue.setCount(count);
		    return ue;
	    } else {
			return null;
		}
	}

	@Override
	public Award getAwardById(Long awardId) {
		return awardDao.selectAwardByAwardId(awardId);
	}

	@Override
	@Transactional
	public AwardExecution addAward(Award award, ImageHolder thumbnail) throws AwardOperationException {
		if(award != null && award.getShopId() != null) {
			// 给award赋上初始值
			award.setCreateTime(new Date());
			award.setLastEditTime(new Date());
			// award默认可用，即出现在前端展示系统中
			award.setEnableStatus(1);
			//缩略图可以为空
			if(thumbnail != null)
				addThumbnail(award, thumbnail);
			int effectNum = awardDao.insertAward(award);
			if(effectNum <= 0)
				throw new AwardOperationException("添加失败");
			return new AwardExecution(AwardStateEnum.SUCCESS, award);
		}
		return new AwardExecution(AwardStateEnum.FAIL);
	}

	@Override
	@Transactional
	public AwardExecution modifyAward(Award award, ImageHolder thumbnail) throws AwardOperationException {
		if(award != null && award.getAwardId() != null) {
			award.setLastEditTime(new Date());
			//如果存在缩略图则先删除
			if(thumbnail != null) {
				Award tempAward = awardDao.selectAwardByAwardId(award.getAwardId());
				if(tempAward.getAwardImg()!=null) {
					ImageUtil.deleteFileOrPath(tempAward.getAwardImg());
				}
				addThumbnail(award, thumbnail);
			}
			try {
				int effectNum = awardDao.updateAwardByAwardId(award);
				if(effectNum <= 0)
					throw new AwardOperationException("更新奖品信息失败");
				return new AwardExecution(AwardStateEnum.SUCCESS, award);
			}catch (Exception e) {
				throw new AwardOperationException("更新奖品信息失败" + e.getMessage());
			}
		}
		return new AwardExecution(AwardStateEnum.FAIL);
	}

	private void addThumbnail(Award award, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(award.getShopId());
		String thumbnailAddr = ImageUtil.generateNomalImage(dest, thumbnail);
		award.setAwardImg(thumbnailAddr);
	}

}
