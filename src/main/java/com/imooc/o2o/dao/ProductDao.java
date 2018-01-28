package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Product;

public interface ProductDao {
    
	/**
	 * 添加商品信息
	 * @param product
	 * @return
	 */
	public int insertProduct(Product product);
	
	/**
	 * 通过商品Id查找商品信息
	 * @param productId
	 * @return
	 */
	public Product queryProductById(Long productId);
	
	/**
	 * 更新商品信息
	 * @param product
	 * @return
	 */
	public int updateProduct(Product product);
	
	/**
	 * 查询一定条件下商品数量
	 * @param ProductCondition
	 * @return 店铺数量
	 */
	public int queryProductCount(@Param("productCondition") Product ProductCondition);
	
	/**
	 * 通过条件模糊查询商品列表信息
	 * 条件有：店铺名 店铺类别(包含上级类别) 区域 状态 拥有者
	 * @param ProductCondition
	 * @param rowindex
	 * @param pageSize
	 * @return 店铺列表信息
	 */
	public List<Product> quertProductList(@Param("productCondition")Product ProductCondition, @Param("rowIndex")int rowIndex,
			@Param("pageSize")int pageSize);
	
	/**
	 * 解除商品与指定商品类别之间的联系
	 * @param productCategoryId
	 * @return 更新影响行数
	 */
	public int updateProductCategoryToNull(Long productCategoryId);
}
