package com.express.admin.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.express.admin.service.AdminService;
import com.express.admin.service.impl.AdminServiceImpl;
import com.express.model.APPMsg;
import com.express.model.APPVersion;
import com.express.model.ShareWeiXin;
import com.express.util.Constant;
import com.express.util.JsonUtil;
import com.express.util.MsgPush;
import com.express.util.SuperAction;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UploadAction extends SuperAction {
	private List<File> upload;
	private List<String> uploadContentType;
	private List<String> uploadFileName;
	APPVersion AppVersion = new APPVersion();
	APPMsg appMsg = new APPMsg();
	AdminService dao = new AdminServiceImpl();
	Map<String , String > dataMap = new HashMap<String, String>();
	
	public List<File> getUpload() {
		return upload;
	}
	public void setUpload(List<File> upload) {
		this.upload = upload;
	}
	public List<String> getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(List<String> uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public List<String> getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(List<String> uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	
	public void updateApp(){
		System.out.println("听说在上传东西");
		String keepPath = "C:/Express/UploadAPK";
		String appType = request.getParameter("appType");
		String operator = request.getParameter("operator");
		String appVersion = request.getParameter("appVersion");
		if(upload==null||"".equals(appType)||appType==null||"".equals(operator)||operator==null||"".equals(appVersion)||appVersion==null){
			System.out.println("错误");
			dataMap.put("status", "fail");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		System.out.println("appType========="+appType);
		
		System.out.println("operator========="+operator);
		
		
		System.out.println("appVersion========="+appVersion);
		File file=new File(keepPath);
		if(!file.exists())
		{
			file.mkdirs();
		} 
		
		System.out.println("============================="+appType);
		if(0<upload.size()){
			try {
				if("0".equals(appType)){
					System.out.println("============================="+appType);
					FileUtils.copyFile(upload.get(0), new File(file, "CityExpressForUser.apk"));
					AppVersion.setAppPath(Constant.PATH_UAPK);
					AppVersion.setAppType("uapp");
					MsgPush.push("", Constant.JPUSH_UCMASTER_SECRET, Constant.JPUSH_UAPPKEY, Constant.PATH_UAPK,appVersion);
				}else {
					System.out.println("============================="+appType);
					FileUtils.copyFile(upload.get(0), new File(file, "CityExpress.apk"));
					AppVersion.setAppPath(Constant.PATH_CAPK);
					AppVersion.setAppType("capp");
					MsgPush.push("", Constant.JPUSH_CCMASTER_SECRET, Constant.JPUSH_CAPPKEY, Constant.PATH_CAPK,appVersion);
				}
				AppVersion.setOperator(operator);
				AppVersion.setDate(new Date());
				AppVersion.setAppVersion(appVersion);
				dao.save(AppVersion);
				dataMap.put("status", "success");
				JsonUtil.writeToResponse(dataMap);
				System.out.println("上传结束");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("上传shibai000");
				dataMap.put("status", "上传失败");
				try {
					JsonUtil.writeToResponse(dataMap);
					System.out.println("上传shibai");
				} catch (JsonGenerationException e1) {
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}
	}
	
	public void uploadPic(){
		
		if(upload==null){
			dataMap.put("status", "fail");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			String keepPath = "C:/Express/images/cover";
			File file=new File(keepPath);
			if(!file.exists())
			{
				System.out.println("文件夹不存在");
				file.mkdirs();
			} 
			if(0<upload.size()){
				long date = new Date().getTime();
				FileUtils.copyFile(upload.get(0), new File(file,date+".jpg"));
				String path = Constant.PATH_COVER+date+".jpg";
				appMsg.setCoverPath(path);
				appMsg.setDate(new Date());
				appMsg.setOperator(request.getParameter("operator"));
				dao.save(appMsg);
				dataMap.put("status", "success");
				JsonUtil.writeToResponse(dataMap );
			}
		} catch (Exception e) {
			System.out.println("上传失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改分享红包内容
	 */
	public void changeHB(){
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		System.out.println("title++:::"+title);
		System.out.println("content++::"+content);
		if(upload==null||title==null||content==null){
			dataMap.put("results", "fail");
			try {
				JsonUtil.writeToResponse(dataMap);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			String keepPath = "C:/Express/images/cover";
			File file=new File(keepPath);
			if(!file.exists())
			{
				System.out.println("文件夹不存在");
				file.mkdirs();
			} 
			if(0<upload.size()){
				ShareWeiXin shareWeiXin = new ShareWeiXin();
				long date = new Date().getTime();
				FileUtils.copyFile(upload.get(0), new File(file,date+".jpg"));
				String path = Constant.PATH_COVER+date+".jpg";
				shareWeiXin.setId("1");
				shareWeiXin.setTitle(title);
				shareWeiXin.setContent(content);
				shareWeiXin.setUrl(Constant.SHARE_COUPON_URL);
				shareWeiXin.setPicPath(path);
				shareWeiXin.setDate(new Date());
				dao.updateShareHB(shareWeiXin);
				dataMap.put("results", "success");
				JsonUtil.writeToResponse(dataMap );
			}
		} catch (Exception e) {
			System.out.println("上传失败");
			e.printStackTrace();
		}
	}
}
