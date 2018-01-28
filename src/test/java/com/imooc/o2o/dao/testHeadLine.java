package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.HeadLine;

public class testHeadLine extends BaseTest{
    @Autowired
    private HeadLineDao headLineDao;
    
    @Test
    public void testQueryHeadLine() {
    	List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
    	assertEquals(0, headLineList.size());
    }
}
