package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.WeChatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;

public class WechatAuthExecution {
	//操作状态
    private int state;
    
    //操作状态描述
    private String stateInfo;
    
    //操作涉及微信账号数量
    private int count;
    
    
    private WeChatAuth wechatAuth;
    
    private List<WeChatAuth> wechatAuthList;
    
    public WechatAuthExecution() {
    	
    }
    
    //操作失败时使用的构造函数
    public WechatAuthExecution(WechatAuthStateEnum wechatAuthStateEnum) {
    	this.state = wechatAuthStateEnum.getState();
    	this.stateInfo = wechatAuthStateEnum.getStateInfo();
    }
    
    //操作成功时使用的构造函数
    public WechatAuthExecution(WechatAuthStateEnum wechatAuthStateEnum, WeChatAuth wechatAuth) {
    	this.state = wechatAuthStateEnum.getState();
    	this.stateInfo = wechatAuthStateEnum.getStateInfo();
    	this.wechatAuth = wechatAuth;
    	this.count = 1;
    }
    
  //操作成功时使用的构造函数
    public WechatAuthExecution(WechatAuthStateEnum wechatAuthStateEnum, List<WeChatAuth> wechatAuthList) {
    	this.state = wechatAuthStateEnum.getState();
    	this.stateInfo = wechatAuthStateEnum.getStateInfo();
    	this.wechatAuthList = wechatAuthList;
    	this.count = wechatAuthList.size();
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

	public WeChatAuth getWechatAuth() {
		return wechatAuth;
	}

	public void setWechatAuth(WeChatAuth wechatAuth) {
		this.wechatAuth = wechatAuth;
	}

	public List<WeChatAuth> getWechatAuthList() {
		return wechatAuthList;
	}

	public void setWechatAuthList(List<WeChatAuth> wechatAuthList) {
		this.wechatAuthList = wechatAuthList;
	}
}
