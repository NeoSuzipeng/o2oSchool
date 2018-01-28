package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exception.ProductOperationException;

public interface ProductService {
    
	/**
	 * 添加商品信息及缩略图和详情图片
	 * @param product 商品信息
	 * @param imageHolder 缩略图信息
	 * @param imageHolderList 详情图信息
	 * @return ProductExecution
	 * @throws ProductOperationException
	 */
	public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList)
			throws ProductOperationException;
	
	/**
	 * 更新商品信息及缩略图和详情图片
	 * @param product 商品信息
	 * @param imageHolder 缩略图信息
	 * @param imageHolderList 详情图信息
	 * @return ProductExecution
	 * @throws ProductOperationException
	 */
	public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList)
			throws ProductOperationException;
	
	/**
	 * 根据商品ID查询商品信息
	 * @param productId
	 * @return
	 */
	public Product getProductById(long productId);
	
	/**
	 * 根据一定条件查询商品列表
	 * 检查是否为空
	 * @param productCondition
	 * @return
	 */
	public ProductExecution getProductList(Product productCondition,int pageIndex ,int pageSize);
}
