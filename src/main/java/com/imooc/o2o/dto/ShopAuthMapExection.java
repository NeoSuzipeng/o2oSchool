package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapEnum;

public class ShopAuthMapExection {
	//操作状态
    private int state;
    
    private String stateInfo;
   
    private int count;
    
    private ShopAuthMap shopAuthMap;
    
    private List<ShopAuthMap> shopAuthMapList;
    
    public ShopAuthMapExection() {
    	
    }
    
    //操作失败时使用的构造函数
    public ShopAuthMapExection(ShopAuthMapEnum shopAuthMapEnum) {
    	this.state = shopAuthMapEnum.getState();
    	this.stateInfo = shopAuthMapEnum.getStateInfo();
    }
    
    //操作成功时使用的构造函数
    public ShopAuthMapExection(ShopAuthMapEnum shopAuthMapEnum, ShopAuthMap ShopAuthMap) {
    	this.state = shopAuthMapEnum.getState();
    	this.stateInfo = shopAuthMapEnum.getStateInfo();
    	this.shopAuthMap = ShopAuthMap;
    	this.count = 1;
    }
    
    //操作成功时使用的构造函数
    public ShopAuthMapExection(ShopAuthMapEnum shopAuthMapEnum, List<ShopAuthMap> shopAuthMapList) {
    	this.state = shopAuthMapEnum.getState();
    	this.stateInfo = shopAuthMapEnum.getStateInfo();
    	this.shopAuthMapList = shopAuthMapList;
    	this.count = shopAuthMapList.size();
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

	public ShopAuthMap getShopAuthMap() {
		return shopAuthMap;
	}

	public void setShopAuthMap(ShopAuthMap shopAuthMap) {
		this.shopAuthMap = shopAuthMap;
	}

	public List<ShopAuthMap> getShopAuthMapList() {
		return shopAuthMapList;
	}

	public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
		this.shopAuthMapList = shopAuthMapList;
	}
	

}
