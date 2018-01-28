package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/frontend")
public class MyPointController {
   
	@Autowired
	private UserShopMapService userShopMapService;
	
	@RequestMapping(value = "/listusershopmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从session里获取顾客信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != -1)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			userShopMapCondition.setUser(user);
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			if(shopName != null && !"".equals(shopName)) {
				userShopMapCondition.getShop().setShopName(shopName);
			}
			// 根据查询条件分页返回用户消费信息
			UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
			modelMap.put("userShopMapList", ue.getUserShopMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "获取信息失败");
		}
		return modelMap;
	}
}
