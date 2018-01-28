package com.imooc.o2o.entity;

import java.util.Date;

public class ProductCategory {
	    // 主键ID
		private Long productCategoryId;
		// 该类别是属于哪个店铺的
		private Long shopId;
		// 类别名
		private String productCategoryName;
		// 权重，越大越排前显示
		private Integer priority;
		// 创建时间
		private Date createTime;
		public Long getProductCategoryId() {
			return productCategoryId;
		}
		public void setProductCategoryId(Long productCategoryId) {
			this.productCategoryId = productCategoryId;
		}
		public Long getShopId() {
			return shopId;
		}
		public void setShopId(Long shopId) {
			this.shopId = shopId;
		}
		public String getProductCategoryName() {
			return productCategoryName;
		}
		public void setProductCategoryName(String productCategoryName) {
			this.productCategoryName = productCategoryName;
		}
		public Integer getPriority() {
			return priority;
		}
		public void setPriority(Integer priority) {
			this.priority = priority;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		@Override
		public String toString() {
			return "ProductCategory [productCategoryId=" + productCategoryId + ", shopId=" + shopId
					+ ", productCategoryName=" + productCategoryName + ", priority=" + priority + ", createTime="
					+ createTime + "]";
		}

		
}
