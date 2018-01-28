package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
    
	/**
	 * 批量添加商品类别
	 * @param ProductCategories
	 * @return int 批量添加条数
	 */
	public int batchInsertProductcategory(List<ProductCategory> ProductCategories);
	/**
	 * 获取商品类别列表
	 * @param productCategoryId
	 * @return
	 */
	public List<ProductCategory> queryProductCategoryList(Long shopId);
	
	/**
	 * 删除商品类别
	 * @param shopId
	 * @param productCategory
	 * @return
	 */
	public int deleteProductCategory(@Param("shopId")Long shopId,@Param("productCategoryId") Long productCategoryId);
}
