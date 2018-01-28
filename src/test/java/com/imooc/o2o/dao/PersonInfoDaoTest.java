package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.PersonInfo;

public class PersonInfoDaoTest extends BaseTest{
    
	@Autowired
	private PersonInfoDao dao;
	
	@Test
	public void testAInsertPersonInfo() {
		PersonInfo person = new PersonInfo();
		person.setName("love you");
		person.setGender("女");
		person.setUserType(1);
		person.setCreateTime(new Date());
		person.setEnableStatus(1);
		int effectNum = dao.insertPersonInfo(person);
		assertEquals(1, effectNum);
	}
	
	@Test
	public void testBQueryPersonInfoById() {
		PersonInfo person = dao.queryPersonInfoById(11L);
		assertNotNull(person);
	}
}
