package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exception.ProductCategoryOperationException;

public interface ProductCategoryService {
    
	/**
	 * 获取dao层返回的特定店铺的商品类别信息
	 * @param shopId
	 * @return 商品类别信息
	 */
	public List<ProductCategory> getProductCategory(Long shopId);
	
	/**
	 * 批量添加商品类别信息
	 * @param productCategories
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategories)
			throws ProductCategoryOperationException;
	/**
	 * 将此类别下的商品里的类别置为空，再删除此类别
	 * @param shopId
	 * @param ProductCategoryId
	 * @return
	 */
	public ProductCategoryExecution deleteProductCategory(Long shopId, Long ProductCategoryId)
			throws ProductCategoryOperationException;
}
