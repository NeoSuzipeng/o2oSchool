package com.imooc.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.LocalAuth;

public interface LocalAuthDao {
    
	/**
	 * 通过用户名和密码查找本地账号
	 * 主要用于登录比较
	 * @param userName
	 * @param password
	 * @return
	 */
	public LocalAuth queryLocalAuthByNameAndPwd(@Param("username")String userName, @Param("password") String password);
	
	/**
	 * 通过用户Id查找本地账号
	 * @param userId
	 * @return
	 */
	public LocalAuth queryLocalAuthByUserId(@Param("userId") long userId);
	
	/**
	 * 插入本地账号信息
	 * 用于本地账号注册
	 * @param locaAuth
	 * @return
	 */
	public int insertLocalAuth(LocalAuth locaAuth);
	
	/**
	 * 更新本地账号信息
	 * 主要用于修改密码
	 * 使用userId,username,password进行定位
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	public int updateLocalAuth(@Param("userId") Long userId, @Param("username") String username,
			@Param("password") String password, @Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
