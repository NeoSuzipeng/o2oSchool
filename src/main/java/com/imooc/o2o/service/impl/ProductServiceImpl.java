package com.imooc.o2o.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exception.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import com.imooc.o2o.dao.ProductImgDao;

@Service
public class ProductServiceImpl implements ProductService{
    
	@Autowired
	private ProductDao productDao;
    
	@Autowired
	private ProductImgDao productImgDao;
	/**
	 * 1.添加缩略图，并将路径返回给product
	 * 2.添加商品信息
	 * 3.添加批量添加详情图片
	 */
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder imageHolder,
			List<ImageHolder> imageHolderList) throws ProductOperationException {
		//保证商品信息不为空， 商品所属店铺不为空 ， 商品所属店铺的店铺Id不为空
		if(product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			product.setCreateTime(new Date());
			//默认商品状态为上架
			product.setEnableStatus(1);
			if(imageHolder != null) {
				addThumbnail(product, imageHolder);
			}
			try {
				//添加商品信息
				 int effectNum = productDao.insertProduct(product);
				 if(effectNum <= 0)
					 throw new ProductOperationException("添加商品信息失败");
			}catch (Exception e) {
				throw new ProductOperationException("添加商品信息失败: " + e.getMessage());
			}
			if(imageHolderList != null && imageHolderList.size() > 0) {
				addImageList(product,imageHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		}else {
			return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
		}
	}
	/**
	 * 批量添加详情图
	 * @param product
	 * @param imageHolderList
	 */
	private void addImageList(Product product, List<ImageHolder> imageHolderList) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<>();
		Long productId = product.getProductId();
		for(ImageHolder imageHolder : imageHolderList) {
			//存储单张详情图并返回存储路径
			String imgAddr = ImageUtil.generateNomalImage(dest, imageHolder);
			ProductImg productImg = new ProductImg();
			productImg.setCreateTime(new Date());
			productImg.setProductId(productId);
			productImg.setImgAddr(imgAddr);
			productImgList.add(productImg);
		}
		if(productImgList.size() > 0) {
			try {
				//批量添加详情图
				int effectNum = productImgDao.batchInsertProductImg(productImgList);
				if(effectNum <= 0) {
					throw new ProductOperationException("添加商品详情图片失败");
				}
			}catch (Exception e) {
				throw new ProductOperationException("添加商品详情图片失败: "+e.getMessage());
			}
		}
	}
	
	/**
	 * 添加缩略图
	 * @param product
	 * @param imageHolder
	 */
	private void addThumbnail(Product product, ImageHolder imageHolder) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String relativePath = ImageUtil.generateThumbnail(dest, imageHolder);
		product.setImgAddr(relativePath);
	}
	
	/**
	 * 1.判断商品不为空,Id不为空（大多数情况为dead code）,店铺不为空，Id不为空
	 * 2.如果缩略图存在则删除缩略图，再更新数据库
	 * 3.详情图的处理与缩略图类似
	 * 4.更新商品信息
	 */
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList)
			throws ProductOperationException {
		if(product != null 
				&& product.getShop() != null && product.getShop().getShopId() != null) {
			try {
				product.setLastEditTime(new Date());
				if(imageHolder != null) {
					//获取原商品信息
					Product tempProduct = productDao.queryProductById(product.getProductId());
					//添加商品时确定缩略图和详情图不为空
					if(tempProduct.getImgAddr() != null) {
						ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
					}
					addThumbnail(product, imageHolder);
				}
				if(imageHolderList != null && imageHolderList.size() > 0) {
					deleteProductImgList(product.getProductId());
					addImageList(product, imageHolderList);
				}
			  //更新商品信息
				int effectNum = productDao.updateProduct(product);
				if(effectNum <= 0) {
					throw new ProductOperationException("更新商品失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS);
			}catch (Exception e) {
				throw new ProductOperationException("更新商品失败: " + e.getMessage());
			}
		}else {
			return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
		}
		
		
	}
	
	/**
	 * 批量删除商品图片
	 * @param productId
	 */
	private void deleteProductImgList(Long productId){
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		for(ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		productImgDao.deleteProductImgByProductId(productId);
	}
	
	/**
	 * 根据商品ID查询商品信息
	 * @param productId
	 * @return
	 */
	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductById(productId);
	}
	
	/**
	 * 根据一定条件查询商品列表
	 * 检查是否为空
	 * @param productCondition
	 * @return
	 */
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex ,int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.quertProductList(productCondition, rowIndex, pageSize);
		ProductExecution productExecution = new ProductExecution(ProductStateEnum.SUCCESS);
		int count = productDao.queryProductCount(productCondition);
		if(productList != null && productList.size() > 0) {
			productExecution.setCount(count);
			productExecution.setProductList(productList);
		}else {
			return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
		}
		return productExecution;
	}

	
}
