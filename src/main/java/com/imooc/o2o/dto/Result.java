package com.imooc.o2o.dto;

/**
 * 封装JSON对象
 * 
 * @author 10353
 *
 * @param <T>
 */
public class Result<T> {

	private T data;

	private boolean success;

	private String errorMsg;

	private int errorCode; // 接受状态码

	// 成功时的构造器
	public Result(T data, boolean success) {
		this.data = data;
		this.success = success;
	}

	// 错误时的构造器
	public Result(boolean success, String errorMsg, int errorCode) {
		this.success = success;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
