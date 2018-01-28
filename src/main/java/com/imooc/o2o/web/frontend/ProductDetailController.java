package com.imooc.o2o.web.frontend;

import java.net.URLEncoder;
import java.util.HashMap;
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
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
	
	
	private Logger logger = LoggerFactory.getLogger(ProductDetailController.class);
	
	@Autowired
	private ProductService productService;

	/**
	 * 根据商品Id获取商品详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前台传递过来的productId
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		// 空值判断
		if (productId != -1) {
			// 根据productId获取商品信息，包含商品详情图列表
			product = productService.getProductById(productId);
			request.getSession().setAttribute("currentProduct", product);
			PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
			if(user != null) {
				modelMap.put("needQRCode", true);
			}else {
				modelMap.put("neddQRCode", false);
			}
			modelMap.put("product", product);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}
	
    private static String urlPrefix = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0ef11c0049518bbb&redirect_uri=";
	
	private static String urlMiddle = "&response_type=code&scope=snsapi_userinfo&state=";
	
	private static String urlSuffix = "#wechat_redirect";
	
	private static String wechatAuthUrl = "http://smyang.s1.natapp.cc/o2o/shopadmin/adduserproductmap";
	
	/**
	 * 生成二维码图片流并返回给前端
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
	    long productId = HttpServletRequestUtil.getLong(request, "productId");
	    PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
		if(productId !=  -1 && user != null && user.getUserId() != null) {
			//获取时间戳用于有效性验证
			long timeStamp = System.currentTimeMillis();
			//设置二维码内容
			//冗余aaa为了后期替换
			String content = "{aaaproductIdaaa:"+ productId + ",aaacustomerIdaaa:" 
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
}
