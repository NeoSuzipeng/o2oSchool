package com.imooc.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductSellDailyService;

/**
 * 指定店铺的用户消费记录
 * @author 10353
 *
 */

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductSellDailyController {
	
	@Autowired
	private ProductSellDailyService productSellDailyService;
	
	                        
	@RequestMapping(value = "/listproductselldailyinfobyshop" , method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductSellDailyInfobyShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Shop shop = (Shop)request.getSession().getAttribute("currentShop");
		if(shop != null && shop.getShopId() != null) {
			ProductSellDaily productSellDailyCondition = new ProductSellDaily();
			productSellDailyCondition.setShop(shop);
			List<ProductSellDaily> productSellDailies = productSellDailyService.listProductSellDaiy(productSellDailyCondition, null, null);
			modelMap.put("productSellDailyList", productSellDailies);
		    modelMap.put("success", true);
		    return modelMap;
		}
		modelMap.put("success", false);
		modelMap.put("errMsg", "请选择店铺");
		return modelMap;
	}
}
