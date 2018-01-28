package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.ShopCategory;

public class ShopCategoryTest extends BaseTest{
    
	@Autowired
	private ShopCategoryDao dao;
	
	@Test
	public void testQueryShopCategory() {
		int effectNum = 1;
		List<ShopCategory> list = dao.queryShopCategory(null);
		assertEquals(effectNum, list.size());
	}
}
