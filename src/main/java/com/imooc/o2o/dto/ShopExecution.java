package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;

/**
 * ShopAuthMap的dto层
 * @author 10353
 *
 */
public class ShopExecution {
	
	//操作状态
    private int state;
    
    //操作状态描述
    private String stateInfo;
    
    //操作涉及店铺数量
    private int count;
    
    //增删改店铺时使用的shop对象
    private Shop shop;
    
    //查询店铺是使用的shop列表
    private List<Shop> shopList;
    
    public ShopExecution() {
    	
    }
    
    //操作失败时使用的构造函数
    public ShopExecution(ShopStateEnum shopStateEnum) {
    	this.state = shopStateEnum.getState();
    	this.stateInfo = shopStateEnum.getStateInfo();
    }
    
    //操作成功时使用的构造函数
    public ShopExecution(ShopStateEnum shopStateEnum, Shop shop) {
    	this.state = shopStateEnum.getState();
    	this.stateInfo = shopStateEnum.getStateInfo();
    	this.shop = shop;
    	this.count = 1;
    }
    
  //操作成功时使用的构造函数
    public ShopExecution(ShopStateEnum shopStateEnum, List<Shop> shopList) {
    	this.state = shopStateEnum.getState();
    	this.stateInfo = shopStateEnum.getStateInfo();
    	this.shopList = shopList;
    	this.count = shopList.size();
    }

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
    
}
