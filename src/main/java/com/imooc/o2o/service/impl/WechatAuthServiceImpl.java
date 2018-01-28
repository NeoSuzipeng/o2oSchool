package com.imooc.o2o.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dao.WechatAuthDao;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WeChatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.exception.WechatOperationException;
import com.imooc.o2o.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService{
	
	private static Logger log = LoggerFactory.getLogger(WechatAuthServiceImpl.class);
	
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;
    
	@Override
	public WeChatAuth getWechatAuthByOpenId(String openId) {
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WeChatAuth wechatauth) throws WechatOperationException {
		/*
		 * 1.判断WeChatAuth是否为空
		 * 2.判断用户是否第一次登陆，标识为WeChatAuth中的PersonInfo中的用户Id为空,如果用户Id为空自动创建用户信息即WeChatAuth中的PersonInfo
		 * 3.添加本地微信账号即WeChatAuth
		 */
		boolean invalidLogin = null == wechatauth || null == wechatauth.getOpenId();
		if(invalidLogin) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		boolean firstLogin = wechatauth.getPersonInfo() != null && wechatauth.getPersonInfo().getUserId() == null;
		if(firstLogin) {
			try {
				PersonInfo person = wechatauth.getPersonInfo();
				person.setCreateTime(new Date());
				person.setEnableStatus(1);
				int effectNum = personInfoDao.insertPersonInfo(person);
				wechatauth.setPersonInfo(person);
				if(effectNum <= 0) {
					throw new WechatOperationException("添加用户失败");
				}
			}catch (Exception e) {
				log.error("insertPersonInfo error:" + e.toString());
				throw new WechatOperationException("insertPersonInfo error: " + e.getMessage());
			}
		}
		
		try {
			//添加本地微信账号
			int effectNum = wechatAuthDao.insertWechatAuth(wechatauth);
			if(effectNum <= 0) {
				throw new WechatOperationException("添加本地微信账号失败");
			}else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS);
			}
		}catch (Exception e) {
			log.error("insertWechatAuth error:" + e.toString());
			throw new WechatOperationException("insertWechatAuth error: " + e.getMessage());
		}
	}

}
