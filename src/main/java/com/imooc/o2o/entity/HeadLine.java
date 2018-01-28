package com.imooc.o2o.entity;

import java.util.Date;

/**
 * 头条实体类
 * @author 10353
 *
 */
public class HeadLine {
    private Long lineId;
    private String lineName;
    //头条链接即需要跳转的页面
    private String lineLink;
    private String lineImg;
    //头条的权重即出现的顺序
    private Integer priority;
    //头条状态：0：不可用  1可用
    private Integer enableStatus;
    private Date createTime;
    private Date lastEditTime;
	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getLineLink() {
		return lineLink;
	}
	public void setLineLink(String lineLink) {
		this.lineLink = lineLink;
	}
	public String getLineImg() {
		return lineImg;
	}
	public void setLineImg(String lineImg) {
		this.lineImg = lineImg;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
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
		return "HeadLine [lineId=" + lineId + ", lineName=" + lineName + ", lineLink=" + lineLink + ", lineImg="
				+ lineImg + ", priority=" + priority + ", enableStatus=" + enableStatus + ", createTime=" + createTime
				+ ", lastEditTime=" + lastEditTime + "]";
	}
    
}
