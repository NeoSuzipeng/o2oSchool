package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.ProductCategory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest{
	
    @Autowired
    private ProductCategoryDao dao;
    
    
    @Test
    public void testBQueryProductCategory() {
    	long shopId = 2L;
    	List<ProductCategory> productCategory = dao.queryProductCategoryList(shopId);
    	assertEquals(2, productCategory.size());
    }
    
    @Test
    public void testABatchInsertProductCategory() {
    	ProductCategory productCategory1 = new ProductCategory();
    	productCategory1.setProductCategoryName("类别6");
    	productCategory1.setPriority(6);
    	productCategory1.setCreateTime(new Date());
    	productCategory1.setShopId(2L);
    	
    	ProductCategory productCategory2 = new ProductCategory();
    	productCategory2.setProductCategoryName("类别7");
    	productCategory2.setPriority(7);
    	productCategory2.setCreateTime(new Date());
    	productCategory2.setShopId(2L);
    	List<ProductCategory> productCategories = new ArrayList<>();
    	productCategories.add(productCategory1);
    	productCategories.add(productCategory2);
    	int effectNum = dao.batchInsertProductcategory(productCategories);
    	
    	assertEquals(2, effectNum);
    	
    }
    
    /**
     * 测试回环
     */
    @Test
    public void testCDeleteProductCategory() {
    	long shopId = 2L;
    	List<ProductCategory> productCategories = dao.queryProductCategoryList(shopId);
    	for(ProductCategory productCategory : productCategories) {
    		if("类别6".equals(productCategory.getProductCategoryName()) || "类别7".equals(productCategory.getProductCategoryName())) {
    			int effectNum = dao.deleteProductCategory(shopId, productCategory.getProductCategoryId());
    			assertEquals(1, effectNum);
    		}
    	}
    }
}
