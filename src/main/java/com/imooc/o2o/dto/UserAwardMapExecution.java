package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;

public class UserAwardMapExecution {
	//操作状态
    private int state;
    
    //操作状态描述
    private String stateInfo;
    
    //操作涉及店铺数量
    private int count;
    
    //增删改店铺时使用的UserAwardMap对象
    private UserAwardMap UserAwardMap;
    
    //查询店铺是使用的UserAwardMap列表
    private List<UserAwardMap> UserAwardMapList;
    
    public UserAwardMapExecution() {
    	
    }
    
    //操作失败时使用的构造函数
    public UserAwardMapExecution(UserAwardMapStateEnum UserAwardMapStateEnum) {
    	this.state = UserAwardMapStateEnum.getState();
    	this.stateInfo = UserAwardMapStateEnum.getStateInfo();
    }
    
    //操作成功时使用的构造函数
    public UserAwardMapExecution(UserAwardMapStateEnum UserAwardMapStateEnum, UserAwardMap UserAwardMap) {
    	this.state = UserAwardMapStateEnum.getState();
    	this.stateInfo = UserAwardMapStateEnum.getStateInfo();
    	this.UserAwardMap = UserAwardMap;
    	this.count = 1;
    }
    
  //操作成功时使用的构造函数
    public UserAwardMapExecution(UserAwardMapStateEnum UserAwardMapStateEnum, List<UserAwardMap> UserAwardMapList) {
    	this.state = UserAwardMapStateEnum.getState();
    	this.stateInfo = UserAwardMapStateEnum.getStateInfo();
    	this.UserAwardMapList = UserAwardMapList;
    	this.count = UserAwardMapList.size();
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

	public UserAwardMap getUserAwardMap() {
		return UserAwardMap;
	}

	public void setUserAwardMap(UserAwardMap UserAwardMap) {
		this.UserAwardMap = UserAwardMap;
	}

	public List<UserAwardMap> getUserAwardMapList() {
		return UserAwardMapList;
	}

	public void setUserAwardMapList(List<UserAwardMap> UserAwardMapList) {
		this.UserAwardMapList = UserAwardMapList;
	}
}
