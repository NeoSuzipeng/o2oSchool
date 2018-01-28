package com.imooc.o2o.entity;
/**
 * 本地账号实体类
 */
import java.util.Date;

public class LocalAuth {
    private Long localAuthId;
    private String username;
    private String password;
    private Date createTime;
    private Date lastEditTime;
    private PersonInfo personInfo;  //本地账号的个人信息，通过UserId跟微信号进行关联
	public Long getLocalAuthId() {
		return localAuthId;
	}
	public void setLocalAuthId(Long localAuthId) {
		this.localAuthId = localAuthId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String userName) {
		this.username = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createDate) {
		this.createTime = createDate;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	@Override
	public String toString() {
		return "LocalAuth [localAuthId=" + localAuthId + ", userName=" + username + ", password=" + password
				+ ", createDate=" + createTime + ", lastEditTime=" + lastEditTime + ", personInfo=" + personInfo + "]";
	}
    
}
