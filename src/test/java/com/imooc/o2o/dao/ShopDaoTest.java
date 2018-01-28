package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;


public class ShopDaoTest extends BaseTest{
    
	@Autowired
	private ShopDao dao;
	
	@Test
	public void testQueryShopCount() {
		Shop shopCondition = new Shop();
		ShopCategory shopCategory = new ShopCategory();
		ShopCategory parent = new ShopCategory();
		parent.setShopCategoryId(1L);
		shopCategory.setParent(parent);
		shopCondition.setShopCategory(shopCategory);
		int count = dao.queryShopCount(shopCondition);
		assertEquals(1, count);
	}
	
	@Test
	public void testQueryShopList() {
		Shop shopCondition = new Shop();
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		shopCondition.setShopCategory(shopCategory);
		List<Shop> shopList = dao.quertShopList(shopCondition, 0, 5);
		assertEquals(5, shopList.size());
	}
	
	@Test
	@Ignore
	public void testInsertShop() {
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
		shop.setShopName("测试的店铺");
		shop.setShopImg("test");
		shop.setShopDesc("test");
		shop.setShopAddr("test");
		shop.setPriority(1);
		shop.setPhone("test");
		shop.setEnableStatus(1);
		shop.setCreateTime(new Date());
		shop.setAdvice("审核中");
		int effectedNum = dao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	
	
	@Test
	@Ignore
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopName("测试的店铺");
		shop.setShopImg("测试图片");
		shop.setShopDesc("测试描述");
		shop.setShopAddr("test");
		shop.setPriority(1);
		shop.setPhone("test");
		shop.setEnableStatus(1);
		shop.setCreateTime(new Date());
		shop.setAdvice("审核中");
		int effectedNum = dao.updateShop(shop);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testQueryShop() {
		Shop shop = dao.queryShopById(1L);
		System.out.println(shop.toString());
	}
}
