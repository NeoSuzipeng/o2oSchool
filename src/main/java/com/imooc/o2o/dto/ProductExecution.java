package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.enums.ProductStateEnum;

public class ProductExecution {
	private int state;

	private String stateInfo;

	private List<Product> ProductList;
    
	private Product product;
	
	private int count;


	public void setCount(int count) {
		this.count = count;
	}
	public ProductExecution() {
		
	}

	//失败时构造
	public ProductExecution(ProductStateEnum ProductStateEnum) {
		this.state = ProductStateEnum.getState();
		this.stateInfo = ProductStateEnum.getStateInfo();
	}
    
	//成功时构造
	public ProductExecution(ProductStateEnum ProductStateEnum, List<Product> ProductList) {
		this.state = ProductStateEnum.getState();
		this.stateInfo = ProductStateEnum.getStateInfo();
		this.ProductList = ProductList;
	}
	
	//成功时构造
		public ProductExecution(ProductStateEnum ProductStateEnum, Product Product) {
			this.state = ProductStateEnum.getState();
			this.stateInfo = ProductStateEnum.getStateInfo();
			this.product = Product;
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

	public List<Product> getProductList() {
		return ProductList;
	}

	public void setProductList(List<Product> ProductList) {
		this.ProductList = ProductList;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}
}
