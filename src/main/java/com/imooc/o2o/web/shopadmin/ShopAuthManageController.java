package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.ShopAuthMapExection;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.WeChatAuth;
import com.imooc.o2o.enums.ShopAuthMapEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManageController {
    
	private Logger logger = LoggerFactory.getLogger(ShopAuthManageController.class);
	
	private static String urlPrefix = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0ef11c0049518bbb&redirect_uri=";
	
	private static String urlMiddle = "&response_type=code&scope=snsapi_userinfo&state=";
	
	private static String urlSuffix = "#wechat_redirect";
	
	private static String wechatAuthUrl = "http://smyang.s1.natapp.cc/o2o/shopadmin/addshopauthmap";
	
	@Autowired
	private ShopAuthMapService shopAuthMapService;
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Autowired
	private PersonInfoService personInfoService;

	@RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
	@ResponseBody           
	private Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		logger.info("店铺权限管理请求到达");
		// 取出分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从Session中获取店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// 分页取出该店铺下面的授权信息列表
			ShopAuthMapExection se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex,
					pageSize);
			logger.debug("获取到了店员列表");
			if(se.getState() == ShopAuthMapEnum.SUCCESS.getState()) {
				modelMap.put("shopAuthMapList", se.getShopAuthMapList());
				modelMap.put("count", se.getCount());
				modelMap.put("success", true);
				return modelMap;
			}
				
		} 
		modelMap.put("success", false);
		modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		return modelMap;
	}
	
	/**
	 * 编辑店员信息是获取店员信息
	 * @param shopAuthId
	 * @return
	 */
	@RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 非空判断
		if (shopAuthId != null && shopAuthId > -1) {
			// 根据前台传入的shopAuthId查找对应的授权信息
			ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
			modelMap.put("shopAuthMap", shopAuthMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopAuthId");
		}
		return modelMap;
	}
	
	/**
	 * 店家更改店员信息或者店员权限
	 * @param shopAuthMapStr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//前端两种状态的判断,如果为true说明前端进行的是删除/恢复权限操作不需要验证码校验
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		if(!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		//ObjectMapper用于JSON字符串转换成ShopAuthMap实例
		ObjectMapper objectMapper = new ObjectMapper();	
		ShopAuthMap shopAuthMap = null;
		try {
			shopAuthMap = objectMapper.readValue(shopAuthMapStr, ShopAuthMap.class);
		} catch (IOException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if(shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
			try {
				if(checkPermission(shopAuthMap.getShopAuthId())) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "您已经是最高权限");
					return modelMap;
				}
				ShopAuthMapExection shopAuthMapExection = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
				if(shopAuthMapExection.getState() == ShopAuthMapEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", shopAuthMapExection.getStateInfo());
				}
			}catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入需要更改的信息");
		}
		return modelMap;
	}
    
	
	/**
	 * 用户扫二维码后添加信息到店铺用户映射表
	 * @param shopAuthMapStr
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addshopauthmap", method = RequestMethod.GET)
	@ResponseBody
	private String addShopAuthMap(String shopAuthMapStr, HttpServletRequest request){
		//获取微信用户信息
		WeChatAuth weChatAuth = getEmployeeInfo(request);
		if(null != weChatAuth) {
			//获取完整的用户信息
			PersonInfo user = personInfoService.getPersonInfoById(weChatAuth.getPersonInfo().getUserId());
			//将用户信息添加到会话中
			request.getSession().setAttribute("user", user);
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
			// 去重校验
			// 获取该店铺下所有的授权信息
			ShopAuthMapExection allMapList = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(), 1, 999);
			List<ShopAuthMap> shopAuthList = allMapList.getShopAuthMapList();
			for (ShopAuthMap sm : shopAuthList) {
				if (sm.getEmployee().getUserId() == user.getUserId())
					return "shop/operationfail";
			}

			try {
				// 根据获取到的内容，添加微信授权信息
				ShopAuthMap shopAuthMap = new ShopAuthMap();
				Shop shop = new Shop();
				shop.setShopId(wechatInfo.getShopId());
				shopAuthMap.setShop(shop);
				shopAuthMap.setEmployee(user);
				shopAuthMap.setTitle("员工");
				shopAuthMap.setTitleFlag(1);
				ShopAuthMapExection se = shopAuthMapService.addShopAuthMap(shopAuthMap);
				if (se.getState() == ShopAuthMapEnum.SUCCESS.getState()) {
					return "shop/operationsuccess";
				} else {
					return "shop/operationfail";
				}
				} catch (RuntimeException e) {
					return "shop/operationfail";
			     }
			}
		    return "shop/operationfail";
		}	
	
	
	private boolean checkQRCodeVaild(WechatInfo wechatInfo) {
		if(wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getShopId() != null) {
			long nowTime = System.currentTimeMillis();
			if(nowTime - wechatInfo.getCreateTime() <= 600000) 
				return true;
			else 
				return false;
		}
		return false;
	}

	private WeChatAuth getEmployeeInfo(HttpServletRequest request) {
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

	/**
	 * 生成二维码图片流并返回给前端
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/generateqrcode4shopauth", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		if(currentShop != null && currentShop.getShopId() != null) {
			//获取时间戳用于有效性验证
			long timeStamp = System.currentTimeMillis();
			//设置二维码内容
			//冗余aaa为了后期替换
			String content = "{aaashopIdaaa:" + currentShop.getShopId() + ",aaacreateTimeaaa:" + timeStamp + "}";
			// 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
			try {
				String longUrl = urlPrefix + wechatAuthUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
				//获取短链接
				String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
				BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(shortUrl, response);
				//将二维码发送到前端
				MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
			} catch (Exception e) {
				logger.error("二维码创建失败");
			}
		}
	}
	/**
	 * 店铺权限验证
	 * 禁止店家修改自身权限
	 * @param shopAuthId
	 * @return
	 */
	private boolean checkPermission(Long shopAuthId) {
		ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
		if(shopAuthMap.getTitleFlag() == 0)
			return true;
		return false;
	}
	
	
}
