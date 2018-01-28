package com.imooc.o2o.web.frontend;

import java.net.URLEncoder;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.web.shopadmin.UserProductManagementController;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {
    
	@Autowired
	private PersonInfoService personInfoService;
	
	@Autowired
	private AwardService awardService;
	
	@Autowired
	private UserAwardMapService userAwardMapService;
	
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;
	
	private static final Logger logger = LoggerFactory.getLogger(MyAwardController.class);
	
	/**
	 * 用户按照条件查找自身领取的奖品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserMapAwardMapsByCustomer(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从session中获取用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
			//用户查询条件
			UserAwardMap userAwardMapCondition = new UserAwardMap();
			userAwardMapCondition.setUser(user);
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			if(null != awardName && "".equals(awardName)) {
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMapCondition.setAward(award);
			}
			UserAwardMapExecution userAwardMapExecution = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
			modelMap.put("userAwardMapList", userAwardMapExecution.getUserAwardMapList());
			modelMap.put("count", userAwardMapExecution.getCount());
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
		}
		return modelMap;
	}
	
	/**
	 * 根据顾客奖品映射Id获取单条顾客奖品的映射信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAwardbyId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前端传递过来的userAwardId
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		// 空值判断
		if (userAwardId > -1) {
			// 根据Id获取顾客奖品的映射信息，进而获取奖品Id
			UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			// 根据奖品Id获取奖品信息
			Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
			// 将奖品信息和领取状态返回给前端
			modelMap.put("award", award);
			modelMap.put("usedStatus", userAwardMap.getUsedStatus());
			modelMap.put("userAwardMap", userAwardMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "获取信息失败");
		}
		return modelMap;
	}

	
	/**
	 * 用户领取指定店铺下的奖品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addUserAwardMap(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从session中获取用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 从前端请求中获取奖品Id
	    Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		// 封装成用户奖品映射对象
		UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
		// 空值判断
		if (userAwardMap != null) {
			try {
				// 添加兑换信息
			    UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
				if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
				} catch (RuntimeException e) {
				    modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
				    return modelMap;
				}

				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "请选择领取的奖品");
				}
		return modelMap;
	}
	
	    private static String urlPrefix = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0ef11c0049518bbb&redirect_uri=";
		
		private static String urlMiddle = "&response_type=code&scope=snsapi_userinfo&state=";
		
		private static String urlSuffix = "#wechat_redirect";
		
		private static String wechatAuthUrl = "http://smyang.s1.natapp.cc/o2o/shopadmin/exchangeaward";
		
		/**
		 * 生成二维码图片流并返回给前端
		 * @param request
		 * @param response
		 */
		@RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
		@ResponseBody
		private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
		    long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		    UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
		    //确保用户处于登录状态
		    PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
			if(userAwardId !=  -1 && user != null && user.getUserId() != null && userAwardMap != null 
					&& userAwardMap.getUser().getUserId() == user.getUserId()) {
				//获取时间戳用于有效性验证
				long timeStamp = System.currentTimeMillis();
				//设置二维码内容
				//冗余aaa为了后期替换
				String content = "{aaauserAwardIdaaa:"+ userAwardId + ",aaacustomerIdaaa:" 
				                             + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
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
	 * 封装用户奖品映射实体类
	 * 
	 * @param user
	 * @param awardId
	 * @return
	 */
	private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
		UserAwardMap userAwardMap = null;
		// 空值判断
		if (user != null && user.getUserId() != null && awardId != -1) {
			userAwardMap = new UserAwardMap();
			// 根据用户Id获取用户实体类对象
			PersonInfo personInfo = personInfoService.getPersonInfoById(user.getUserId());
			// 根据奖品Id获取奖品实体类对象
			Award award = awardService.getAwardById(awardId);
			userAwardMap.setUser(personInfo);
			userAwardMap.setAward(award);
			Shop shop = new Shop();
			shop.setShopId(award.getShopId());
			userAwardMap.setShop(shop);
			//添加默认店家为操作员
			List<ShopAuthMap> shopAuthMaps = shopAuthMapDao.queryShopAuthMapListByShopId(shop.getShopId(), 0, 999);
			for(ShopAuthMap shopAuthMap : shopAuthMaps) {
				if(shopAuthMap.getTitleFlag() == 0) {
					PersonInfo operator = new PersonInfo();
					operator.setUserId(shopAuthMap.getEmployee().getUserId());
					userAwardMap.setOperator(operator);
				}
			}
			
			// 设置积分
			userAwardMap.setPoint(award.getPoint());
			userAwardMap.setCreateTime(new Date());
			// 设置兑换状态为已领取
			userAwardMap.setUsedStatus(1);
		}
		return userAwardMap;
	}
}
