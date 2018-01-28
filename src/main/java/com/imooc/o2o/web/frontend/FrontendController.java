package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
	/**
	 * 首页路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}

	/**
	 * 商品列表页路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	private String showShopList() {
		return "frontend/shoplist";
	}
	
	/**
	 * 店铺详情页路由
	 * @return
	 */
	@RequestMapping(value="/shopdetail", method = RequestMethod.GET)
	private String showShopDetail() {
		return "frontend/shopdetail";
	}
	
	/**
	 * 商品详情路由
	 * @return
	 */
	@RequestMapping(value="/productdetail", method = RequestMethod.GET)
	private String showProductDetail() {
		return "frontend/productdetail";
	}
	
	/**
	 * 店铺奖品列表
	 * @return
	 */
	@RequestMapping(value="/awardlist", method = RequestMethod.GET)
	private String awardlist() {
		return "frontend/awardlist";
	}
	
	/**
	 * 用户奖品详情页
	 * @return
	 */
	@RequestMapping(value="/myawarddetail", method = RequestMethod.GET)
	private String myawarddetail() {
		return "frontend/myawarddetail";
	}
	
	/**
	 * 用户奖品列表页
	 * @return
	 */
	@RequestMapping(value="/pointrecord", method = RequestMethod.GET)
	private String pointrecord() {
		return "frontend/pointrecord";
	}
	
	/**
	 * 用户消费记录列表页
	 * @return
	 */
	@RequestMapping(value="/myrecord", method = RequestMethod.GET)
	private String myrecord() {
		return "frontend/myrecord";
	}
	
	/**
	 * 用户积分列表页面
	 * @return
	 */
	@RequestMapping(value="/mypoint", method = RequestMethod.GET)
	private String mypoint() {
		return "frontend/mypoint";
	}
	
	
}
