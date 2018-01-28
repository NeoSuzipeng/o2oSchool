package com.imooc.o2o.entity;
/**
 * 区域实体类
 */
import java.util.Date;

public class Area {
	//区域ID
    private Integer areaId;
    //权重
    private Integer priority;
    //区域名称
    private String areaName;
    //创建时间
    private Date createTime;
    //修改时间
    private Date lastEditTime;
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	@Override
	public String toString() {
		return "Area [areaId=" + areaId + ", priority=" + priority + ", areaName=" + areaName + ", createTime="
				+ createTime + ", lastEditTime=" + lastEditTime + "]";
	}
    
}
