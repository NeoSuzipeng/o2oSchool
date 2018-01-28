package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.Award;
import com.imooc.o2o.enums.AwardStateEnum;

public class AwardExecution {
	//操作状态
    private int state;
    
    //操作状态描述
    private String stateInfo;
    
    //操作涉及店铺数量
    private int count;
    
    //增删改店铺时使用的Award对象
    private Award Award;
    
    //查询店铺是使用的Award列表
    private List<Award> AwardList;
    
    public AwardExecution() {
    	
    }
    
    //操作失败时使用的构造函数
    public AwardExecution(AwardStateEnum AwardStateEnum) {
    	this.state = AwardStateEnum.getState();
    	this.stateInfo = AwardStateEnum.getStateInfo();
    }
    
    //操作成功时使用的构造函数
    public AwardExecution(AwardStateEnum AwardStateEnum, Award Award) {
    	this.state = AwardStateEnum.getState();
    	this.stateInfo = AwardStateEnum.getStateInfo();
    	this.Award = Award;
    	this.count = 1;
    }
    
  //操作成功时使用的构造函数
    public AwardExecution(AwardStateEnum AwardStateEnum, List<Award> AwardList) {
    	this.state = AwardStateEnum.getState();
    	this.stateInfo = AwardStateEnum.getStateInfo();
    	this.AwardList = AwardList;
    	this.count = AwardList.size();
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

	public Award getAward() {
		return Award;
	}

	public void setAward(Award Award) {
		this.Award = Award;
	}

	public List<Award> getAwardList() {
		return AwardList;
	}

	public void setAwardList(List<Award> AwardList) {
		this.AwardList = AwardList;
	}
}
