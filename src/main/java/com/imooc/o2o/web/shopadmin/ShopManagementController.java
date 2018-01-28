package com.imooc.o2o.web.shopadmin;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ProductOperationException;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    
	private static Logger logger = LoggerFactory.getLogger(ShopManagementController.class);
	
	@Autowired
	private ShopService shopService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private AreaService areaService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	
	//可以上传的最大图片数量
	private final static int IMAGE_MAX_COUNT = 6;
    
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getproductlistbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前端传来的店家信息
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		if(currentShop == null) {
			modelMap.put("success", false);
		    modelMap.put("errMsg", "您不没有查询权限");
		    logger.debug("current shop is null");
		    return modelMap;
		}
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		if(pageIndex > -1 && pageIndex > -1 && currentShop != null && currentShop.getShopId() != null) {
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = compactProductCodition(currentShop.getShopId(),productCategoryId,productName);
			ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("success", true);
			modelMap.put("count", productExecution.getCount());
			modelMap.put("productList", productExecution.getProductList());
		}else {
		      modelMap.put("success", false);
		      modelMap.put("errMsg", "查询失败");
		}
		return modelMap;
	}
	/**
	 * 构建商品列表的查询条件
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductCodition(Long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId > -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName != null) {
			productCondition.setProductName(productName);
		}
		return productCondition;
	}
    /**
     * 根据商品ID进行查询商品信息
     * @param productId
     * @return
     */
	@RequestMapping(value="/getproductbyId", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(productId > 0) {
			Product product = productService.getProductById(productId);
			if(product != null) {
				List<ProductCategory> productCategories = productCategoryService.getProductCategory(
						product.getShop().getShopId());
				modelMap.put("success", true);
				modelMap.put("productCategoryList", productCategories);
				modelMap.put("product", product);
				return modelMap;
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "至少选择一件商品");
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "非法商品Id");
			return modelMap;
		}
	}
	/**
	 * 更新商品信息（包含整体信息更新和上下架更新）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//判断是否为上下架操作
		Boolean changeStatus = HttpServletRequestUtil.getBoolean(request, "statusChange");
		if(!changeStatus && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "验证码错误");
			return modelMap;
		}
		//接受前端发送的商品信息,缩略图信息，详情图信息
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		ImageHolder imageHolder = null;
		List<ImageHolder> productImgList = new ArrayList<>();
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver
				(request.getSession().getServletContext());
		//初始化信息
		try {
			//判断是否为Multipart类型
			if(commonsMultipartResolver.isMultipart(request)) {
				imageHolder = handleImage(request, productImgList);
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "解析图片失败" + e.getMessage());
			return modelMap;
		}
		//添加信息
		try {
			//解析商品实体类
			product = mapper.readValue(productStr, Product.class);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg",e.toString());
			return modelMap;
		}
		if(product != null) {
			try {
				Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//核心：添加商品信息及缩略图信息和详情图片信息
				ProductExecution productExecution = productService.modifyProduct(product, imageHolder, productImgList);
				if(productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productExecution.getStateInfo());
				}
			}catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errorMsg",e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errorMsg","请输入商品信息");
			return modelMap;
		}
		return modelMap;
	}
    /**
     * 针对前端传来的文件流进行封装，并在本地存储
     * @param request
     * @param productImgList
     * @return
     * @throws IOException
     */
	private ImageHolder handleImage(HttpServletRequest request, List<ImageHolder> productImgList) throws IOException {
		MultipartHttpServletRequest multipartRequest;
		ImageHolder imageHolder;
		multipartRequest = (MultipartHttpServletRequest)request;
		//获取请求中的上传文件
		CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile)multipartRequest.getFile("thumbnail");
		//构建ImageHodler对象
		imageHolder = new ImageHolder
				(commonsMultipartFile.getOriginalFilename(), commonsMultipartFile.getInputStream());
		CommonsMultipartFile productImgFile = null;
		for(int i = 0; i < IMAGE_MAX_COUNT; i++) {
			productImgFile = (CommonsMultipartFile)multipartRequest.getFile("productImg"+i);
			if(productImgFile != null) {
				ImageHolder productImg = new ImageHolder
						(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
				productImgList.add(productImg);
			}else {
				//如果商品详情图为空跳出循环
				break;
			}
		}
		return imageHolder;
	}
	
	/**
	 * 添加商品信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> addProduct(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "验证码错误");
			return modelMap;
		}
		//接受前端发送的商品信息,缩略图信息，详情图信息
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		ImageHolder imageHolder = null;
		List<ImageHolder> productImgList = new ArrayList<>();
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver
				(request.getSession().getServletContext());
		//初始化信息
		try {
			//判断是否为Multipart类型
			if(commonsMultipartResolver.isMultipart(request)) {
				imageHolder = handleImage(request, productImgList);
			}else {
				modelMap.put("success", false);
				modelMap.put("errorMsg", "上传图片不能为空" );
				return modelMap;
			}
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "解析图片失败" + e.getMessage());
			return modelMap;
		}
		//添加信息
		try {
			//解析商品实体类
			product = mapper.readValue(productStr, Product.class);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg",e.toString());
			return modelMap;
		}
		if(product != null && imageHolder != null && productImgList.size() >= 0) {
			try {
				Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
				Shop shop = new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				//核心：添加商品信息及缩略图信息和详情图片信息
				ProductExecution productExecution = productService.addProduct(product, imageHolder, productImgList);
				if(productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productExecution.getStateInfo());
				}
			}catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errorMsg",e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errorMsg","请输入商品信息");
			return modelMap;
		}
		return modelMap;
	}
	
	/**
	 * 返回店铺管理员信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			} else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			logger.debug(currentShop.toString());
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	/**
	 * 针对单个用户获取店铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
		//2.根据用户信息获取店铺列表
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			List<Shop> shopList = shopService.getShopList(shopCondition, 0, 100).getShopList();
			request.getSession().setAttribute("shopList", shopList);
			modelMap.put("success", true);
			modelMap.put("shopList", shopList);
			modelMap.put("user", user);
		}catch(Exception e) {
			modelMap.put("success", true);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	/**
	 * 修改店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		// 返回数据模型
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "验证码错误");
			return modelMap;
		}
		// 1.接受并转换前端发送的信息
		// 1.1 shop对象信息处理
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (IOException e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", e.getMessage());
			return modelMap;
		}
		// 1.2 图片信息处理
		CommonsMultipartFile shopImg = null;
		// 注册CommonsMultipartFile处理器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}

		// 2.修改店铺
		if (shop != null && shop.getShopId() != null) {
			ShopExecution shopExecution;
			try {
				// web传输文件名的获取需要使用getOriginalFileName()
				if(shopImg == null)
					shopExecution = shopService.modifyShop(shop, null);
				else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					shopExecution = shopService.modifyShop(shop, imageHolder);
				}
				if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState())
					modelMap.put("success", true);
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errorMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errorMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "店铺ID不能为空");
			return modelMap;
		}
	}
	
	
	/**
	 * 通过店铺ID查找店铺信息
	 * 返回给客户端
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshopbyid", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId > -1) {
			try {
				Shop shop = shopService.getShopById(shopId);
				List<Area> areaList = areaService.getQueryArea();
				modelMap.put("shop", shop);
				modelMap.put("areaList",areaList);
				modelMap.put("success", true);
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		return modelMap;
	}
	/**
	 * 获取店铺注册所需的选择信息 如：店铺类别 区域
	 * 
	 * @return
	 */
	@RequestMapping("/getshopinitinfo")
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategorys = new ArrayList<>();
		List<Area> areas = new ArrayList<>();
		try {
			shopCategorys = shopCategoryService.getQueryShopCategory(new ShopCategory());
			areas = areaService.getQueryArea();
			modelMap.put("success", true);
			modelMap.put("areaList", areas);
			modelMap.put("shopCategoryList", shopCategorys);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 店铺注册 :1.接受并转换前端发送的信息 2.注册店铺 3.返回Map转换后的JSON对象
	 * 
	 * @param request
	 * @return Map<String, Object> 经过@ResponseBody转化为JSON对象发送给前端
	 */
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		// 返回数据模型
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "验证码错误");
			return modelMap;
		}
		// 1.接受并转换前端发送的信息
		// 1.1 shop对象信息处理
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (IOException e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", e.getMessage());
			return modelMap;
		}
		// 1.2 图片信息处理
		CommonsMultipartFile shopImg = null;
		// 注册CommonsMultipartFile处理器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "图片不能为空");
			return modelMap;
		}

		// 2.注册店铺
		if (shop != null && shopImg != null) {
			PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
			shop.setOwner(owner);
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream()); 
				// web传输文件名的获取需要使用getOriginalFileName()
				ShopExecution shopExecution = shopService.addShop(shop, imageHolder);
				if (shopExecution.getState() == ShopStateEnum.CHECK.getState())
					modelMap.put("success", true);
			   //获取用户的店铺列表
				@SuppressWarnings("unchecked")
				List<Shop> shopList =  (List<Shop>)request.getSession().getAttribute("shopList");
				if(shopList == null || shopList.size() == 0)
					shopList = new ArrayList<>();
				//将新建的店铺信息添加到用户的店铺列表
				shopList.add(shop);
				request.getSession().setAttribute("shopList", shopList);
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errorMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errorMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errorMsg", "请输入店铺信息");
			return modelMap;
		}
	}
}
