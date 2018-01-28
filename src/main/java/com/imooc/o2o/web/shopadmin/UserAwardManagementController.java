package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopAuthMapExection;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.entity.WeChatAuth;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
    
	@Autowired
	private UserAwardMapService userAwardMapService;
	
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserAwardManagementController.class);
	
	@RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapsByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从session里获取店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserAwardMap userAwardMap = new UserAwardMap();
			userAwardMap.setShop(currentShop);
			// 从请求中获取奖品名
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			if (awardName != null) {
				// 如果需要按照奖品名称搜索，则添加搜索条件
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMap.setAward(award);
			}
			// 分页返回结果
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMap, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "参数为空");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/exchangeaward", method = RequestMethod.GET)
	@ResponseBody
	private String exchangeAward(HttpServletRequest request) {
		//获取微信用户信息
				WeChatAuth weChatAuth = getOperatorInfo(request);
				if(null != weChatAuth) {
					PersonInfo operator = weChatAuth.getPersonInfo();
					request.getSession().setAttribute("user", operator);
					//解析微信回传的自定义参数，将content进行解码
					String qrCodeinfo = null;
					try {
						qrCodeinfo = new String(
							URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						return "shop/operationfail";
					}
					ObjectMapper mapper = new ObjectMapper();
					WechatInfo wechatInfo = null;  //封装二维码操作需要的各类ID（例如shopId customerId ...）
					try {
						wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
					} catch (IOException e) {
						return "shop/operationfail";
					}
					if(!checkQRCodeVaild(wechatInfo)) {
						return "shop/operationfail";
					}
					Long userAwardId = wechatInfo.getUserAwardId();
					Long customerId = wechatInfo.getCustomerId();
					UserAwardMap userAwardMap = compactUserAwardMap4Add(customerId, userAwardId, operator);
					
					//空值检查
					if(userAwardId != null && customerId != null) {
						try {
							//检查操作员是否有权限
							if(!CheckShopAuth(operator.getUserId(), userAwardMap)) {
								return "shop/operationfail";
							}
							UserAwardMapExecution upe = userAwardMapService.modifyUserAwardMap(userAwardMap);
							if(upe.getState() == UserProductMapStateEnum.SUCCESS.getState())
								return "shop/operationsuccess";
						
						}catch (Exception e) {
							return "shop/operationfail";
						}
						
					}
				}
				return "shop/operationfail";
	}
	


	private UserAwardMap compactUserAwardMap4Add(Long customerId, Long userAwardId, PersonInfo operator) {
		UserAwardMap userAwardMap = null;
		// 空值判断
	    if (customerId != null && userAwardId != null && operator != null) {
			// 获取原有userAwardMap信息
			userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			userAwardMap.setUsedStatus(1);
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			userAwardMap.setUser(customer);
			userAwardMap.setOperator(operator);
		}
	    return userAwardMap;
	}

	/**
	 * 检查操作员权限
	 * @param userId
	 * @param userProductMap
	 * @return
	 */
	private boolean CheckShopAuth(Long userId, UserAwardMap userAwardMap) {
		ShopAuthMapExection shopAuthMapExection = shopAuthMapService.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 0, 999);
		for(ShopAuthMap shopAuthMap : shopAuthMapExection.getShopAuthMapList()) {
			if(shopAuthMap.getEmployee().getUserId() == userId)
				return true;
		}
		return false;
	}
	
	private boolean checkQRCodeVaild(WechatInfo wechatInfo) {
		if(wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getUserAwardId() != null && wechatInfo.getCustomerId() != null) {
			long nowTime = System.currentTimeMillis();
		if(nowTime - wechatInfo.getCreateTime() <= 600000) 
			return true;
		else 
			return false;
		}
			return false;
	}

    private WeChatAuth getOperatorInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WeChatAuth auth = null;
		if(null != code) {
			UserAccessToken token = null;
		    try {
				token = WechatUtil.getUserAccessToken(code);
				String openId = token.getOpenId();
				request.getSession().setAttribute("openId", openId);
			    auth = wechatAuthService.getWechatAuthByOpenId(openId);
			}catch (IOException e) {
				logger.error("获取token失败");
			}
	    }
			return auth;
	}
}
