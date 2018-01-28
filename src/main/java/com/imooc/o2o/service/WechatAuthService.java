package com.imooc.o2o.service;

import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.WeChatAuth;
import com.imooc.o2o.exception.WechatOperationException;

public interface WechatAuthService {
    
	/**
	 * 通过本平台用户的OpenId查询对应的微信账号
	 * @param openId
	 * @return
	 */
	public WeChatAuth getWechatAuthByOpenId(String openId);
	
	/**
	 * 注册本平台微信账号
	 * @param wechatauth
	 * @return
	 * @throws WechatOperationException
	 */
	public WechatAuthExecution register(WeChatAuth wechatauth)throws WechatOperationException;
}
