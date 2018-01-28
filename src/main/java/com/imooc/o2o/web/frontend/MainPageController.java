package com.imooc.o2o.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value="/frontend")
public class MainPageController {
    
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private HeadLineService headLineService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private ShopService shopService;
	/**
	 * 初始化前端展示系统的主页信息，包括头条信息，一级类别信息
	 */
	@RequestMapping(value="listmainpageinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listMainPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<>();
		try {
			//条件为null,即返回一级类别信息
			shopCategoryList = shopCategoryService.getQueryShopCategory(null);
			modelMap.put("shopCategoryList", shopCategoryList);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		List<HeadLine> headLineList = new ArrayList<>();
		try {
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
	
	/**
	 * 返回前端选择的商品类别列表
	 * 情况1.点击全部商店时，返回全部父类别为空的类别
	 * 情况2.点击二级类别时，返回二级类别下的子类别
	 * 返回可选择的全部区域类别
	 */
	@RequestMapping(value="/listshopspageinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		List<ShopCategory> shopCategoryList = null;
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		if(parentId != -1) {
			//情况2.点击二级类别时，返回二级类别下的子类别
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getQueryShopCategory(shopCategoryCondition);
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}else {
			//情况1.点击全部商店时，返回全部父类别为空的类别
			try {
				shopCategoryList = shopCategoryService.getQueryShopCategory(null);
			}catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		
		List<Area> areaList = null;
		try {
			//获取区域信息
			areaList = areaService.getQueryArea();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
	}
	
	/**
	 * 根据查询条件查询对应的店铺信息
	 * 1.前端点击‘全部类别’,发送parentId查询此父类别下二级类别的所有店铺信息
	 * 2.前端点击‘二级类别’，发送shopCategoryId查询此类别下的所有店铺信息
	 * 3.前端点击‘某区域’,发送areaId查询此区域下的所有店铺信息
	 * 4.前端输入店铺名关键字，发送name查询此关键字的所有店铺信息
	 * 5.4种查询方式组合
	 * 获取分页信息进行分页
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listshops", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		if((pageIndex > -1) && (pageSize > -1)) {
			Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			Long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			Shop shopCondition = compactShopConditionSearch(parentId,shopCategoryId,areaId,shopName);
			ShopExecution shopList = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("success", true);
			modelMap.put("shopList", shopList.getShopList());
			modelMap.put("count", shopList.getCount());
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "选择您想要的页面");
		}
		return modelMap;
	}
    /**
     * 组合查询条件
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
	private Shop compactShopConditionSearch(Long parentId, Long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		//查询条件：指定一级类别下二级类别的全部店铺信息
		if(parentId != -1L) {
			ShopCategory parentCategory = new ShopCategory();
			ShopCategory childCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		//查询条件：指定二级类别下的全部店铺信息
		if(shopCategoryId != -1L) {
			ShopCategory shopCategory = new ShopCategory();
			shopCondition.setShopCategory(shopCategory);
		}
		//查询条件:指定区域的全部店铺信息
		if(areaId != -1) {
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		//查询条件：模糊店铺名下的所有店铺信息
		if(!"".equals(shopName)) {
			shopCondition.setShopName(shopName);
		}
		//显示店铺都是审核通过的
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
	
	
}
