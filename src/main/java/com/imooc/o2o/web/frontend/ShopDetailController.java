package com.imooc.o2o.web.frontend;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
    
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	private Logger logger = LoggerFactory.getLogger(ShopDetailController.class);
	
	/**
	 * 根据前端提供的店铺ID查询店铺信息即店铺下商品所属的全部商品类别
	 */
	@RequestMapping(value="/listshopdetailpageinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		if(shopId != -1L) {
			try {
				shop = shopService.getShopById(shopId);
				productCategoryList = productCategoryService.getProductCategory(shopId);
				request.getSession().setAttribute("currentShop", shop);
				modelMap.put("success", true);
				modelMap.put("shop", shop);
				modelMap.put("productCategoryList", productCategoryList);
			}catch(Exception e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg","empty shopId");
		}
		return modelMap;
	}
	
	/**
	 * 根据前端提供的查询条件查询商品列表
	 * 查询条件1：指定店铺ID
	 * 查询条件2：指定商品类别
	 * 查询条件3：模糊商品名
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listproductsbyshop", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
	    if(shopId != -1L && pageSize != -1L && pageIndex != -1L) {
	    	try {
	    		//获取查询条件
		    	// 尝试获取商品类别Id
		    	long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
		    	// 尝试获取模糊查找的商品名
		    	String productName = HttpServletRequestUtil.getString(request, "productName");
		    	Product productCondition = compactProductSearchCondition(shopId, productCategoryId, productName);
		    	ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
		    	modelMap.put("success", true);
		    	modelMap.put("productList", productExecution.getProductList());
		    	modelMap.put("count", productExecution.getCount());
	    	}catch (Exception e) {
	    		modelMap.put("success", false);
		    	modelMap.put("errMsg", e.getMessage());
		    	return modelMap;
			}
	    }else {
	    	modelMap.put("success", true);
	    	modelMap.put("errMsg", "empty shopId or pagesize or pageindex");
	    }
		return modelMap;
	}
    
	/**
	 * 组合商品查询条件
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductSearchCondition(Long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if (productCategoryId != -1L) {
			// 查询某个商品类别下面的商品列表
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if (productName != null) {
			// 查询名字里包含productName的店铺列表
			productCondition.setProductName(productName);
		}
		// 只允许选出状态为上架的商品
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
