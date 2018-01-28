package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineDao {
    /**
     * 根据条件查询头条信息
     * @param headLineCondition
     * @return
     */
	public List<HeadLine> queryHeadLine(@Param("headLineCondition")HeadLine headLineCondition);
}
