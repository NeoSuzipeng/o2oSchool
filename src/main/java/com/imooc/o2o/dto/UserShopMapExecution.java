package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserShopMapStateEnum;

public class UserShopMapExecution {
	//操作状态
    private int state;
    
    //操作状态描述
    private String stateInfo;
    
    //操作涉及店铺数量
    private int count;
    
    //增删改店铺时使用的UserShopMap对象
    private UserShopMap UserShopMap;
    
    //查询店铺是使用的UserShopMap列表
    private List<UserShopMap> UserShopMapList;
    
    public UserShopMapExecution() {
    	
    }
    
    //操作失败时使用的构造函数
    public UserShopMapExecution(UserShopMapStateEnum UserShopMapStateEnum) {
    	this.state = UserShopMapStateEnum.getState();
    	this.stateInfo = UserShopMapStateEnum.getStateInfo();
    }
    
    //操作成功时使用的构造函数
    public UserShopMapExecution(UserShopMapStateEnum UserShopMapStateEnum, UserShopMap UserShopMap) {
    	this.state = UserShopMapStateEnum.getState();
    	this.stateInfo = UserShopMapStateEnum.getStateInfo();
    	this.UserShopMap = UserShopMap;
    	this.count = 1;
    }
    
  //操作成功时使用的构造函数
    public UserShopMapExecution(UserShopMapStateEnum UserShopMapStateEnum, List<UserShopMap> UserShopMapList) {
    	this.state = UserShopMapStateEnum.getState();
    	this.stateInfo = UserShopMapStateEnum.getStateInfo();
    	this.UserShopMapList = UserShopMapList;
    	this.count = UserShopMapList.size();
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

	public UserShopMap getUserShopMap() {
		return UserShopMap;
	}

	public void setUserShopMap(UserShopMap UserShopMap) {
		this.UserShopMap = UserShopMap;
	}

	public List<UserShopMap> getUserShopMapList() {
		return UserShopMapList;
	}

	public void setUserShopMapList(List<UserShopMap> UserShopMapList) {
		this.UserShopMapList = UserShopMapList;
	}
}
