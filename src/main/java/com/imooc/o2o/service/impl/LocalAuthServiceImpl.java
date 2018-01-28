package com.imooc.o2o.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exception.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService{
    
	private Logger logger = LoggerFactory.getLogger(LocalAuthServiceImpl.class);
	
	@Autowired
	private LocalAuthDao localAuthDao;
	
	
	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
		return localAuthDao.queryLocalAuthByNameAndPwd(userName, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalAuthByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		boolean localAuthInfoEmpty =  null == localAuth || null == localAuth.getPassword() || null == localAuth.getPassword()
				|| null == localAuth.getPersonInfo().getUserId();
		if(localAuthInfoEmpty)
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		//检查是否已经绑定过微信账号,保证唯一性
		LocalAuth checkAuth = localAuthDao.queryLocalAuthByUserId(localAuth.getPersonInfo().getUserId());
		if(null != checkAuth)
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectNum = localAuthDao.insertLocalAuth(localAuth);
			if(effectNum <= 0)
				return new LocalAuthExecution(LocalAuthStateEnum.LOGINFAIL);
			else
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
		}catch (Exception e) {
			logger.error("insertLocalAuth fail: " + e.getMessage());
			throw new LocalAuthOperationException("insertLocalAuth error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) {
	    boolean notEmptyInfo = null != userId && null != username && null != password && null != newPassword;
		if(notEmptyInfo) {
			try {
				// 更新密码，并对新密码进行MD5加密
				int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password),
						MD5.getMd5(newPassword), new Date());
				// 判断更新是否成功
				if (effectedNum <= 0) {
					throw new LocalAuthOperationException("更新密码失败");
				}
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			}catch (Exception e) {
				logger.error("update localAuth password fail: " + e.getMessage());
				throw new LocalAuthOperationException("update localAuth error: " + e.getMessage());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_UPDATE_INFO);
		}
	}

}
