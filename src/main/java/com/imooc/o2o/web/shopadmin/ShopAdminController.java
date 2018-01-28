package com.imooc.o2o.web.shopadmin;
/**
 * 请求转发控制器
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin", method=RequestMethod.GET)
public class ShopAdminController {
    
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}
	
	@RequestMapping(value="/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}
	
	@RequestMapping(value="/shopmanagement")
	public String shopManage() {
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value="/productcategorymanagement")
	public String productCategory() {
		return "shop/productcategorymanagement";
	}
	
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}
	
	@RequestMapping(value="/productmanagemenet")
	public String productManagemenet() {
		return "shop/productmanagemenet";
	}
	
	@RequestMapping(value="/shopauthmanagement")
	public String shopauthmanagement() {
		return "shop/shopauthmanagement";
	}
	
	@RequestMapping(value="/shopauthedit")
	public String shopauthedit() {
		return "shop/shopauthedit";
	}
	
	@RequestMapping(value = "/productbuycheck")
	public String productbuycheck() {
		return "shop/productbuycheck";
	}
	
	@RequestMapping(value = "/usershopcheck")
	public String usershopcheck() {
		return "shop/usershopcheck";
	}
	
	@RequestMapping(value = "/awarddelivercheck")
	public String awarddelivercheck() {
		return "shop/awarddelivercheck";
	}
	
	@RequestMapping(value = "/awardmanagement")
	public String awardmanagement() {
		return "shop/awardmanagement";
	}
	
	@RequestMapping(value = "/awardoperation")
	public String awardoperation() {
		return "shop/awardoperation";
	}
}
