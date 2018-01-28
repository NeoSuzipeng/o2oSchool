package com.imooc.o2o.dto;
/**
 * 封装扫描二维码后微信传回的各类ID
 * @author 10353
 *
 */
public class WechatInfo {
	
    private Long customerId;
    
    private Long shopId;
    
    private Long userAwardId;
    
    private Long productId;
    
    private Long createTime;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getUserAwardId() {
		return userAwardId;
	}

	public void setUserAwardId(Long userAwardId) {
		this.userAwardId = userAwardId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
    
    
}
