package com.imooc.o2o.entity;
/**
 * 微信账号实体类
 */
import java.util.Date;

public class WeChatAuth {
	//微信账号Id
    private Long weChatAuthId;
    //公众号关联Id
    private String openId;
    private Date createTime;
    //微信账号拥有者
    private PersonInfo personInfo;
	public Long getWeChatAuthId() {
		return weChatAuthId;
	}
	public void setWeChatAuthId(Long weChatAuthId) {
		this.weChatAuthId = weChatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	@Override
	public String toString() {
		return "WeChatAuth [weChatAuthId=" + weChatAuthId + ", openId=" + openId + ", createTime=" + createTime
				+ ", personInfo=" + personInfo + "]";
	}
    
  
}
