package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.ShopService;

public class ShopServiceTest extends BaseTest{
    
	@Autowired
	private ShopService shopService;
	
	@Test
	public void testModifyShop() throws FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopName("俺俩也");
		File file = new File("D:\\QQfile\\image\\dabai.jpg");
		InputStream ins = new FileInputStream(file);
		String shopImgName = file.getName();
		ImageHolder imageHolder = new ImageHolder(shopImgName, ins);
		shopService.modifyShop(shop, imageHolder);
	}
	
	@Test
	@Ignore
	public void testAddShop() {
		PersonInfo owner = new PersonInfo();
		ShopCategory shopCategory = new ShopCategory();
		Area area = new Area();
		Shop shop = new Shop();
		owner.setUserId(1L);
		shopCategory.setShopCategoryId(1L);
		area.setAreaId(2);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺3");
		shop.setShopDesc("test3");
		shop.setShopAddr("test3");
		shop.setPriority(1);
		shop.setPhone("test3");
		shop.setAdvice("审核中");
		File file = new File("C:\\Users\\10353\\Pictures\\Saved Pictures\\111.jpg");
	    InputStream ins = null;
		try {
			ins = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    String fileName = file.getName();
	    ImageHolder imageHolder = new ImageHolder(fileName, ins);
		ShopExecution shopExecution = shopService.addShop(shop,imageHolder);
		assertEquals(0, shopExecution.getState());
	}
}
