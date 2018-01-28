package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.enums.UserProductMapStateEnum;

public class UserProductMapExecution {
	//操作状态
    private int state;
    
    //操作状态描述
    private String stateInfo;
    
    //操作涉及店铺数量
    private int count;
    
    //增删改店铺时使用的UserProductMap对象
    private UserProductMap UserProductMap;
    
    //查询店铺是使用的UserProductMap列表
    private List<UserProductMap> UserProductMapList;
    
    public UserProductMapExecution() {
    	
    }
    
    //操作失败时使用的构造函数
    public UserProductMapExecution(UserProductMapStateEnum UserProductMapStateEnum) {
    	this.state = UserProductMapStateEnum.getState();
    	this.stateInfo = UserProductMapStateEnum.getStateInfo();
    }
    
    //操作成功时使用的构造函数
    public UserProductMapExecution(UserProductMapStateEnum UserProductMapStateEnum, UserProductMap UserProductMap) {
    	this.state = UserProductMapStateEnum.getState();
    	this.stateInfo = UserProductMapStateEnum.getStateInfo();
    	this.UserProductMap = UserProductMap;
    	this.count = 1;
    }
    
  //操作成功时使用的构造函数
    public UserProductMapExecution(UserProductMapStateEnum UserProductMapStateEnum, List<UserProductMap> UserProductMapList) {
    	this.state = UserProductMapStateEnum.getState();
    	this.stateInfo = UserProductMapStateEnum.getStateInfo();
    	this.UserProductMapList = UserProductMapList;
    	this.count = UserProductMapList.size();
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

	public UserProductMap getUserProductMap() {
		return UserProductMap;
	}

	public void setUserProductMap(UserProductMap userProductMap) {
		UserProductMap = userProductMap;
	}

	public List<UserProductMap> getUserProductMapList() {
		return UserProductMapList;
	}

	public void setUserProductMapList(List<UserProductMap> userProductMapList) {
		UserProductMapList = userProductMapList;
	}
   
	
}
