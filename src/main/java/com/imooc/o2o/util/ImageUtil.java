package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.imooc.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
    
	
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static final Random FILE_RANDOM = new Random();
	
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	/**
	 * 将CommonsMultipartFile类型对象转换为File
	 * @param multipartFile
	 */
	public File transferCommonsMultipartFileToFile(CommonsMultipartFile multipartFile) {
		String orginFileName = multipartFile.getOriginalFilename();
		File dest = new File(orginFileName);
		try {
			multipartFile.transferTo(dest);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return dest;
	}
	
	/**
	 * 处理缩略图，并返回新生成图片的相对值路径
	 * @param file 
	 * @param targetAddr
	 * @return the image of relatively address to save
	 */
	public static String generateThumbnail(String targetAddr, ImageHolder imageHolder) {
		//获取图片的随机名称且名称不冲突
		String randomName = getRandomFileName();
		//获取图片的拓展名
		String expand = getFileExpandName(imageHolder.getImageName());
		//如果目标地址为空，创建目标地址
		makeDirPath(targetAddr);
		//组合相对路径
		String relativePath = targetAddr + randomName + expand;
		logger.debug("相对路径: " + relativePath);
		//获取文件保存路径
		File file = new File(PathUtil.getBasePathUtil() + relativePath);
		logger.debug("绝对路径: " + file.getAbsolutePath());
		try {
			Thumbnails.of(imageHolder.getImageInputSteam()).size(200, 200)
			  .watermark(Positions.BOTTOM_LEFT,ImageIO.read(new File(basePath + "/watermark.jpg")), 0.2f)
			  .outputQuality(0.8f).toFile(file);
		}catch(IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		//返回图片相对路径
		return relativePath;
	}
    /**
     * 创建目标文件夹
     * @param targetAddr
     */
	private static void makeDirPath(String targetAddr) {
		if(targetAddr == null) {			
			logger.debug("targetAddr is empty");
		}
		String realFileParentPath = PathUtil.getBasePathUtil() + targetAddr;
		File realFile = new File(realFileParentPath);
		//目标地址不存在
		if(!realFile.exists()) {
			realFile.mkdirs();
		}
	}
    /**
     * 获取文件的拓展名
     * @param file
     * @return expandName of file
     */
	private static String getFileExpandName(String name) {
		return name.substring(name.lastIndexOf("."));
	}
    
	/**
	 * 生成随机文件名
	 * 规则：当前年月日小时分钟秒钟+5位随机数
	 * @return
	 */
	private static String getRandomFileName() {
		int randomNum = FILE_RANDOM.nextInt(89999) + 10000;
		String nowDateStr = DATE_FORMAT.format(new Date());
		return nowDateStr + randomNum;
	}
	/**
	 * 删除图片操作
	 * 如果参数是文件路径则删除图片
	 * 如果参数是目录路径则删除目录下的所有图片
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getBasePathUtil() + storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File[] files = fileOrPath.listFiles();
				for(int i = 0; i < files.length; i++) {
					files[i].delete();
					logger.debug(files[i].getName() + "删除成功");
				}
			}
			fileOrPath.delete();
			logger.debug(fileOrPath.getName() + "删除成功");
		}else{
			logger.debug("未找到对应元素");
		}
	}
    /**
     * 处理正常尺寸的图，并返回新生成图片的相对值路径
     * @param targetAddr
     * @param imageHolder
     * @return
     */
	public static String generateNomalImage(String targetAddr, ImageHolder imageHolder) {
		//获取图片的随机名称且名称不冲突
				String randomName = getRandomFileName();
				//获取图片的拓展名
				String expand = getFileExpandName(imageHolder.getImageName());
				//如果目标地址为空，创建目标地址
				makeDirPath(targetAddr);
				//组合相对路径
				String relativePath = targetAddr + randomName + expand;
				logger.debug("相对路径: " + relativePath);
				//获取文件保存路径
				File file = new File(PathUtil.getBasePathUtil() + relativePath);
				logger.debug("绝对路径: " + file.getAbsolutePath());
				try {
					Thumbnails.of(imageHolder.getImageInputSteam()).size(337, 600)
					  .watermark(Positions.BOTTOM_LEFT,ImageIO.read(new File(basePath + "/watermark.jpg")), 0.2f)
					  .outputQuality(0.9f).toFile(file);
				}catch(IOException e) {
					logger.error(e.toString());
					e.printStackTrace();
				}
				//返回图片相对路径
				return relativePath;
	}
//	public static void main(String[] args) {
//	File file = new File("F:\\google\\license-1.0.0.txt");
//	System.out.println(getFileExpandName(file));
//	System.out.println(getRandomFileName());
//}
}
