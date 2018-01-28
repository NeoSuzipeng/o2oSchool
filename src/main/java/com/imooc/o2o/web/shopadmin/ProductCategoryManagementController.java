package com.imooc.o2o.web.shopadmin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.service.ProductCategoryService;

/**
 * 商品管理是店家的权限
 * 路由：shopadmin
 * @author 10353
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value="/getproductcategorylist", method=RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){	
		
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		List<ProductCategory> productCategories = null;
		if(currentShop != null && currentShop.getShopId() > 0) {
			productCategories = productCategoryService.getProductCategory(currentShop.getShopId());
			return  new Result<List<ProductCategory>>(productCategories, true);
		}else {
			ProductCategoryStateEnum productCategoryStateEnum = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, productCategoryStateEnum.getStateInfo(),
					productCategoryStateEnum.getState());
		}
	}
	
	@RequestMapping(value="/addproductcategory", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategory(@RequestBody List<ProductCategory> productCategories, 
			HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		//1.获取会话中的店铺信息
		Shop currentShop = (Shop )request.getSession().getAttribute("currentShop");
		if(productCategories != null && productCategories.size() > 0) {
			//2.向商品类别中装填shopId
			for(ProductCategory productCategory : productCategories) {
				productCategory.setShopId(currentShop.getShopId());
				productCategory.setCreateTime(new Date());
			}
			//3.批量添加
			try {
				ProductCategoryExecution productCategoryExecution = 
						productCategoryService.batchAddProductCategory(productCategories);
				if(productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success",false);
					modelMap.put("errMsg",productCategoryExecution.getStateInfo());
				}
			}catch (Exception e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请至少输入一个商品类别");
		}
		
		return modelMap;
	}
	
	@RequestMapping(value="/removeproductcategory", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId, 
			HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		//1.获取会话中的店铺信息
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		if(productCategoryId != null && productCategoryId > 0) {
			//2.删除商品信息类别
			try {
				ProductCategoryExecution productCategoryExecution = 
						productCategoryService.deleteProductCategory(currentShop.getShopId(), productCategoryId);
				if(productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success",false);
					modelMap.put("errMsg",productCategoryExecution.getStateInfo());
				}
			}catch (Exception e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errMsg","请至少选择一个商品类别");
		}
		
		return modelMap;
	}
}
