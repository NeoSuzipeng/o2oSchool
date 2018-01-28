package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exception.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
    
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private ProductDao productDao;
	
	/**
	 * 获取dao层返回的特定店铺的商品类别信息
	 * @param shopId
	 * @return 商品类别信息
	 */
	@Override
	public List<ProductCategory> getProductCategory(Long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategories)
			throws ProductCategoryOperationException {
		if(productCategories != null && productCategories.size() > 0) {
			try {
				int effectNum = productCategoryDao.batchInsertProductcategory(productCategories);
				if(effectNum <= 0) {
					throw new ProductCategoryOperationException("商品类别创建失败");
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			}catch (Exception e) {
				throw new ProductCategoryOperationException("批量添加失败:"+e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}
    /**
     * 删除指定商品ID的商品类别
     * 1.删除商品与商品类别之间的联系
     * 2.删除商品类别
     */
	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(Long shopId, Long productCategoryId)
			throws ProductCategoryOperationException{
		try {
			int effectNum = productDao.updateProductCategoryToNull(productCategoryId);
			if(effectNum < 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("商品类别删除失败"+e.getMessage());
		}
		try {
			int effectNum = productCategoryDao.deleteProductCategory(shopId, productCategoryId);
			if(effectNum <= 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch (Exception e) {
			throw new ProductCategoryOperationException("商品类别删除失败"+e.getMessage());
		}
	}

}
