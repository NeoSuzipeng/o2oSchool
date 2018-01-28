package com.imooc.o2o.entity;

import java.util.Date;

/**
 * 用户领取商品
 * @author 10353
 *
 */
public class UserAwardMap {
    
	private Long userAwardId;
	
	//奖品兑换状态
	private Integer usedStatus;
	
	private Date createTime;
	
	//奖品所需积分
	private Integer point;
	
	private PersonInfo user;
	
	//用户领取的奖品
	private Award award;
	
	//奖品所属商店
	private Shop shop;
	
	//操作员
	private PersonInfo operator;
    
	
	public PersonInfo getOperator() {
		return operator;
	}

	public void setOperator(PersonInfo operator) {
		this.operator = operator;
	}

	public Long getUserAwardId() {
		return userAwardId;
	}

	public void setUserAwardId(Long userAwardId) {
		this.userAwardId = userAwardId;
	}

	public Integer getUsedStatus() {
		return usedStatus;
	}

	public void setUsedStatus(Integer usedStatus) {
		this.usedStatus = usedStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public PersonInfo getUser() {
		return user;
	}

	public void setUser(PersonInfo user) {
		this.user = user;
	}

	public Award getAward() {
		return award;
	}

	public void setAward(Award awrad) {
		this.award = awrad;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}
