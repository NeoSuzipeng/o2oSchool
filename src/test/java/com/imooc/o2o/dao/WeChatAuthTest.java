package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WeChatAuth;

public class WeChatAuthTest extends BaseTest{
    
	@Autowired
	private WechatAuthDao dao;
	
	@Test
	public void testInsertWechatAuth() {
		WeChatAuth wechat = new WeChatAuth();
		wechat.setOpenId("dasdasgdf");
		wechat.setCreateTime(new Date());
		PersonInfo person = new PersonInfo();
		person.setUserId(11L);
		wechat.setPersonInfo(person);
		int effectNum = dao.insertWechatAuth(wechat);
		assertEquals(1, effectNum);
	}
	
	@Test
	public void testQueryWechatInfoByOpenId() {
		WeChatAuth wechat = dao.queryWechatInfoByOpenId("dasdasgdf");
		assertNotNull(wechat);
	}
}
