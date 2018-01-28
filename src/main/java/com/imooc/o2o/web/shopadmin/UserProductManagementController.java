package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

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
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.entity.WeChatAuth;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;


/**
 * 用户消费记录管理
 * 1.查询某店铺下用户消费记录
 * 2.扫码后添加用户消费记录
 * @author 10353
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {
    
	private static final Logger logger = LoggerFactory.getLogger(UserProductManagementController.class);
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	
	@Autowired
	private UserProductMapService userProductMapService;
	/**
	 * 扫码后添加用户消费记录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)
	@ResponseBody
	private String addUserProductMap(HttpServletRequest request){
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
			Long productId = wechatInfo.getProductId();
			Long customerId = wechatInfo.getCustomerId();
			UserProductMap userProductMap = compactUserProductMap4Add(customerId, productId, operator);
			
			//空值检查
			if(productId != null && customerId != null) {
				try {
					//检查操作员是否有权限
					if(!CheckShopAuth(operator.getUserId(), userProductMap)) {
						return "shop/operationfail";
					}
					UserProductMapExecution upe = userProductMapService.addUserProductMap(userProductMap);
					if(upe.getState() == UserProductMapStateEnum.SUCCESS.getState())
						return "shop/operationsuccess";
				
				}catch (Exception e) {
					return "shop/operationfail";
				}
				
			}
		}
		return "shop/operationfail";
	}
    
	/**
	 * 检查操作员权限
	 * @param userId
	 * @param userProductMap
	 * @return
	 */
	private boolean CheckShopAuth(Long userId, UserProductMap userProductMap) {
		ShopAuthMapExection shopAuthMapExection = shopAuthMapService.listShopAuthMapByShopId(userProductMap.getShop().getShopId(), 0, 999);
		for(ShopAuthMap shopAuthMap : shopAuthMapExection.getShopAuthMapList()) {
			if(shopAuthMap.getEmployee().getUserId() == userId)
				return true;
		}
		return false;
	}

	/**
	 * 根据传入的customerId, productId以及操作员信息组建用户消费记录
	 * 
	 * @param customerId
	 * @param productId
	 * @param operator
	 * @return
	 */
	private UserProductMap compactUserProductMap4Add(Long customerId, Long productId, PersonInfo operator) {
		UserProductMap userProductMap = null;
		if (customerId != null && productId != null) {
			userProductMap = new UserProductMap();
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			// 主要为了获取商品积分
			Product product = productService.getProductById(productId);
			userProductMap.setProduct(product);
			userProductMap.setShop(product.getShop());
			userProductMap.setUser(customer);
			userProductMap.setPoint(product.getPoint());
			userProductMap.setCreateTime(new Date());
			userProductMap.setOperator(operator);
		}
		return userProductMap;
	}
				
	private boolean checkQRCodeVaild(WechatInfo wechatInfo) {
		if(wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getProductId() != null && wechatInfo.getCustomerId() != null) {
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
