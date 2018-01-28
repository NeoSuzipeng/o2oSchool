package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoDao {
    
	/**
	 * 通过用户Id查询用户
	 * @param useId
	 * @return
	 */
	public PersonInfo queryPersonInfoById(long useId);
	
	/**
	 * 插入用户信息
	 * @param personinfo
	 * @return
	 */
	public int insertPersonInfo(PersonInfo personinfo);
	
}
