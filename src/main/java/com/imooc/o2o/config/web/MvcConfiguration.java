package com.imooc.o2o.config.web;


import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.imooc.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.imooc.o2o.interceptor.shopadmin.ShopPermissionInterceptor;


@Configuration
@EnableWebMvc //等价于<mvc:annotation-driven />
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{
    
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 配置静态资源
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
	    registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/baidu/work/image/upload/");
	}
	
	/**
	 * 配置默认Sevlet
	 *  对静态资源默认servlet配置，加入 对.js .gif .img等资源的处理， 允许使用"/"进行整体映射   
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
    
	/**
	 * 配置视图解析器
	 */
	@Bean(name="viewResolver")
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		//取消缓存
		internalResourceViewResolver.setCache(false);
		internalResourceViewResolver.setPrefix("/WEB-INF/html/");
		internalResourceViewResolver.setSuffix(".html");
		return internalResourceViewResolver;
	}
    
	
	@Value("${kaptcha.border}")
	private String border;

	@Value("${kaptcha.textproducer.font.color}")
	private String fcolor;

	@Value("${kaptcha.image.width}")
	private String width;

	@Value("${kaptcha.textproducer.char.string}")
	private String cString;

	@Value("${kaptcha.image.height}")
	private String height;

	@Value("${kaptcha.textproducer.font.size}")
	private String fsize;

	@Value("${kaptcha.noise.color}")
	private String nColor;

	@Value("${kaptcha.textproducer.char.length}")
	private String clength;

	@Value("${kaptcha.textproducer.font.names}")
	private String fnames;

	/**
	 * 由于web.xml不生效了，需要在这里配置Kaptcha验证码Servlet
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() throws ServletException {
		ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/Kaptcha");
		servlet.addInitParameter("kaptcha.border", border);// 无边框
		servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor); // 字体颜色
		servlet.addInitParameter("kaptcha.image.width", width);// 图片宽度
		servlet.addInitParameter("kaptcha.textproducer.char.string", cString);// 使用哪些字符生成验证码
		servlet.addInitParameter("kaptcha.image.height", height);// 图片高度
		servlet.addInitParameter("kaptcha.textproducer.font.size", fsize);// 字体大小
		servlet.addInitParameter("kaptcha.noise.color", nColor);// 干扰线的颜色
		servlet.addInitParameter("kaptcha.textproducer.char.length", clength);// 字符个数
		servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);// 字体
		return servlet;
	}
	
	/**
	 * 配置文件上传解析器
	 * @return
	 */
	@Bean(name="commonsMultipartResolver")
	public CommonsMultipartResolver createCommonsMultipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSize(20971520);
		commonsMultipartResolver.setMaxUploadSizePerFile(20971520);
		return commonsMultipartResolver;
	}
	
	/**
	 * 配置拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration loginInterceptor =registry.addInterceptor(new ShopLoginInterceptor());
		String shopLoginInterceptPath = "/shopadmin/**";
		
		//登录权限限定
		loginInterceptor.addPathPatterns(shopLoginInterceptPath);
		loginInterceptor.excludePathPatterns("shopadmin/addshopauthmap");
		loginInterceptor.excludePathPatterns("/shopadmin/exchangeaward");
		loginInterceptor.excludePathPatterns("/shopadmin/adduserproductmap");
		
		
		InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
		// 配置拦截的路径
		permissionIR.addPathPatterns(shopLoginInterceptPath);
		// 配置不拦截的路径
		/** shoplist page **/
		permissionIR.excludePathPatterns("/shopadmin/shoplist");
		permissionIR.excludePathPatterns("/shopadmin/getshoplist");
		/** shopregister page **/
		permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
		permissionIR.excludePathPatterns("/shopadmin/registershop");
		permissionIR.excludePathPatterns("/shopadmin/shopoperation");
		/** shopmanage page **/
		permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
		permissionIR.excludePathPatterns("/shopadmin/getmanagementinfo");
		/** shopauthmanagement page **/
		permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");
		/** scan **/
		permissionIR.excludePathPatterns("/shopadmin/adduserproductmap");
		permissionIR.excludePathPatterns("/shopadmin/exchangeaward");
	}
	
}
