package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.entity.ProductImg;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest{
    
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	public void testABatchProductImg(){
		ProductImg productImg1 = new ProductImg();
		productImg1.setProductId(1L);
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setImgAddr("测试1");
		ProductImg productImg2 = new ProductImg();
		productImg2.setProductId(1L);
		productImg2.setPriority(2);
		productImg2.setCreateTime(new Date());
		productImg2.setImgAddr("测试2");
		List<ProductImg> productImgList = new ArrayList<>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int effectNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectNum);
	}
	@Test
	public void testBQueryProductImgList() {
		List<ProductImg> list = productImgDao.queryProductImgList(1L);
		assertEquals(2, list.size());
	}
	@Test
	public void testDeleteProductImgByProductId() {
		int effectNum = productImgDao.deleteProductImgByProductId(1L);
		assertEquals(2, effectNum);
	}
}
