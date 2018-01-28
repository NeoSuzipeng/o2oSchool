package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductImg;

public interface ProductImgDao {
    
	/**
	 * 批量添加商品详情图片
	 * @param productImgList
	 * @return
	 */
	public int batchInsertProductImg(List<ProductImg> productImgList);
	
	/**
	 * 通过商品Id删除商品所有详情图
	 * @param productId
	 * @return
	 */
	public int deleteProductImgByProductId(Long productId);

	/**
	 * 通过商品Id查询商品详情图
	 * @param productId
	 * @return
	 */
	public List<ProductImg> queryProductImgList(Long productId);
}
