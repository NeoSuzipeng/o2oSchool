package com.imooc.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.dto.WechatUser;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WeChatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
    
	 private static Logger logger = LoggerFactory.getLogger(WechatController.class);
	 
	 private static final String FONTTEND = "1";
	 
	 private static final String ADMIN = "2";
	 
	 @Autowired
	 private WechatAuthService wechatAuthService;
	 
	 @Autowired
	 private PersonInfoService personInfoService;
	 
	 /**
	  * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0ef11c0049518bbb&redirect_uri=http://smyang.s1.natapp.cc/o2o/wechatlogin/logincheck&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping(value="/logincheck",method= {RequestMethod.GET})
	 public String doGet(HttpServletRequest request, HttpServletResponse response) {
		 logger.debug("weixin get...");
		 String code = HttpServletRequestUtil.getString(request, "code");
		 logger.debug("code: " + code);
		 String roleType = HttpServletRequestUtil.getString(request, "state");
	     logger.debug("roleType: " + roleType);			 
		 WechatUser user = null;
		 String openId = null;
		 WeChatAuth auth = null;
		 if(null != code) {
			 UserAccessToken token;
			 try {
				token = WechatUtil.getUserAccessToken(code);
				logger.debug("weixin login toke: "+ token);
				String accessToken = token.getAccessToken();
				openId = token.getOpenId();
				user = WechatUtil.getUserInfo(accessToken, openId);
				logger.debug("weixin login user: " + user.toString());
				request.getSession().setAttribute("openId", openId);
				//检查是否已存在微信账号
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 if(auth == null) {
			 auth = new WeChatAuth();
			 auth.setOpenId(openId);
			 PersonInfo person = WechatUtil.getPersonInfoFromRequest(user);
			 person.setEnableStatus(1);
			 if(FONTTEND.equals(roleType)) {
				 person.setUserType(1);
			 }else {
				 person.setUserType(2);
			 }
			 auth.setPersonInfo(person);
			 WechatAuthExecution wechatAuthExecution = wechatAuthService.register(auth);
			 if(wechatAuthExecution.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				 return null;
			 }else{
				 PersonInfo personInfo = personInfoService.getPersonInfoById(person.getUserId());
				 request.setAttribute("user", personInfo);
			 }
		 }
		 if(ADMIN.equals(roleType)){
			 return "shop/shoplist";
		 }else {
			 return "frontend/index";
		 }
	 }
	
}
