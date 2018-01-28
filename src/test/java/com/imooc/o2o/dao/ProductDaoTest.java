package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest{
    
	@Autowired
	private ProductDao productDao;
	
	@Test
	public void testAInsertProduct() {
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		product.setShop(shop);
		product.setProductName("测试商品1");
		product.setCreateTime(new Date());
		product.setEnableStatus(0);
		product.setNormalPrice("15");
		product.setPriority(1);
		int effectNum = productDao.insertProduct(product);
		assertEquals(1, effectNum);
	}
	
	@Test
	public void testBQueryProductById() {
		Product resultProduct = productDao.queryProductById(1L);
		System.out.println(resultProduct);
	}
	
	@Test
	public void testCQueryProductList() {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		productCondition.setShop(shop);
		int effectNum = productDao.queryProductCount(productCondition);
		List<Product> productList = productDao.quertProductList(productCondition, 0, 5);
		assertEquals(5, productList.size());
		assertEquals(9, effectNum);
	}
	
	@Test 
	public void testDUpdateProduct() {
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		product.setShop(shop);
		product.setProductName("更新后测试商品1");
		product.setCreateTime(new Date());
		product.setEnableStatus(0);
		product.setNormalPrice("10");
		product.setPriority(1);
		product.setProductId(1L);
		productDao.updateProduct(product);
	}
	
	@Test
	public void testUpdateProductCategoryToNull() {
		int effectNum = productDao.updateProductCategoryToNull(1L);
		assertEquals(1, effectNum);
	}
	
}
