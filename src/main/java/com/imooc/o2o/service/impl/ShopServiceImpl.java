package com.imooc.o2o.service.impl;
/**
 * 店鋪service层
 * 1.add店铺信息
 */


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exception.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;


@Service
public class ShopServiceImpl implements ShopService{
    
	private Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);
	
	@Autowired
	private ShopDao shopDao;
	
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;
	
	
	/**
	 * 添加店铺信息
	 */
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder imageHolder)throws ShopOperationException {
		//shop对象空值判断
		if(shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//默认状态为审核中
			shop.setEnableStatus(ShopStateEnum.CHECK.getState());;
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			int effectNum = shopDao.insertShop(shop);
			if(effectNum <= 0) {
				logger.debug("插入店铺失败");
				throw new ShopOperationException("插入店铺失败");
			}else {
				if(imageHolder.getImageInputSteam() != null) {
					try {
						//将图片存储到本地
						addShopImg(shop, imageHolder);
					}catch(Exception e) {
						String errorMsg = "图片存储失败" + e.getMessage();
						logger.error(errorMsg);
						throw new ShopOperationException(errorMsg);
					}
					effectNum = shopDao.updateShop(shop);
					if(effectNum <= 0) {
						String msg = "更新图片地址失败";
						logger.error(msg);
						throw new ShopOperationException(msg);
					}
				}else {
					logger.debug("图片为空");
				}
			}
			//将用户添加到店铺用户表中
			ShopAuthMap shopAuthMap = new ShopAuthMap();
			shopAuthMap.setCreateTime(new Date());
			shopAuthMap.setEmployee(shop.getOwner());
			shopAuthMap.setTitleFlag(0);
			shopAuthMap.setTitle("店家");
			shopAuthMap.setShop(shop);
			effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
			if(effectNum <= 0)
				throw new ShopOperationException("插入店铺失败");
		}catch(Exception e) {
			String errorMsg = "添加店铺失败" + e.getMessage();
			logger.error(errorMsg);
			throw new ShopOperationException(errorMsg);
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}
    
	/**
	 * 1.将传入图片存储在本地文件夹
	 *   默认目录：D:/projectdev/image/upload/image/items/shop/${shopId}
	 *   默认文件名：当前年月日小时分钟秒钟+5位随机数
	 * 2.将存储路径保存到shop对象
	 * @param shop
	 * @param shopImgInputStream
	 */
	private void addShopImg(Shop shop, ImageHolder imageHolder) {
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(dest,imageHolder);
		shop.setShopImg(shopImgAddr);
	}
   
	/**
	 * 获取店铺信息
	 * 依据店铺的ID
	 */
	@Override
	public Shop getShopById(long shopId) {
		return shopDao.queryShopById(shopId);
	}
    
	/**
	 * 更新店铺的信息
	 */
	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder)throws ShopOperationException{
		String shopImgName = imageHolder.getImageName();
		//1.检查是否需要修改图片信息
		if(shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			try {
			if(imageHolder.getImageInputSteam() != null && !"".equals(shopImgName) || shopImgName != null) {
				Shop tempShop = shopDao.queryShopById(shop.getShopId());
				if(tempShop.getShopImg() != null) 
					ImageUtil.deleteFileOrPath(tempShop.getShopImg());
				addShopImg(shop, imageHolder);
			}
			//2.修改店铺信息
			shop.setLastEditTime(new Date());
			int effectNum = shopDao.updateShop(shop);
			if(effectNum <= 0)
				return new ShopExecution(ShopStateEnum.INNERT_ERROR);
			else {
				//3.更新后的值返回
				shop = shopDao.queryShopById(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS, shop);
			}
			}catch(Exception e) {
				throw new ShopOperationException("modify error: " + e.getMessage());
			}
		}
	}
    
	/**
	 * 获取一定条件下的店铺列表
	 * 将参数pageIndex转换为数据库查询起始条数
	 * @param shopCondition 
	 * @param pageIndex 
	 * @param pageSize
	 * @return ShopExecution
	 */
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//页码转换行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		//获取条件下的店铺列表
		List<Shop> shopList = shopDao.quertShopList(shopCondition, rowIndex, pageSize);
		//获取条件下的店铺数量
		int count = shopDao.queryShopCount(shopCondition);
		//店铺条件为空的情况
		ShopExecution shopExecution = new ShopExecution();
		if(shopList != null) {
			shopExecution.setShopList(shopList);
			shopExecution.setCount(count);
		}else {
			shopExecution.setState(ShopStateEnum.INNERT_ERROR.getState());
		}
		return shopExecution;
	}
   
}
