package com.imooc.o2o.dao;

import com.imooc.o2o.entity.WeChatAuth;

public interface WechatAuthDao {
    
	/**
	 * 通过OpenId查询本平台的微信账号
	 * @param openId
	 */
	public WeChatAuth queryWechatInfoByOpenId(String openId);
	
	/**
	 * 添加本平台的微信账号
	 * @param wechatauth
	 * @return
	 */
	public int insertWechatAuth(WeChatAuth wechatauth);
}
