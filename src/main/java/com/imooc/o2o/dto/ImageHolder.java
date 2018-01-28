package com.imooc.o2o.dto;

import java.io.InputStream;

public class ImageHolder {
	
    private String imageName;
    
    private InputStream imageInputSteam;

	public ImageHolder(String imageName, InputStream imageInputSteam) {
		super();
		this.imageName = imageName;
		this.imageInputSteam = imageInputSteam;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImageInputSteam() {
		return imageInputSteam;
	}

	public void setImageInputSteam(InputStream imageInputSteam) {
		this.imageInputSteam = imageInputSteam;
	}
    
    
}
