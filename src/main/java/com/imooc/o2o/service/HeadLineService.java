package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineService {
    /**
     * 实现头条信息的查询
     * @param headLineCondition
     * @return
     */
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
