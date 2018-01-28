package com.imooc.o2o.interceptor.shopadmin;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.imooc.o2o.entity.Shop;

/**
 * 店铺管理权限拦截器
 * 试图非法获取某一店铺的权限请求
 * 终止操作
 * @author 10353
 *
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	/**
	 * 基本逻辑：
	 * 取出会话中当前店铺信息和用户可操作店铺列表
	 * 如果当前店铺信息不再可操作店铺列表中终止操作
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        @SuppressWarnings("unchecked")
		List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
        for(Shop shop: shopList) {
        	if(shop.getShopId() == currentShop.getShopId())
        		return true;
        }
		return false;
	}
}
