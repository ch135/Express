package com.express.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * 获取某省城市列表
 * @author LK
 *
 */
public class CityUtil {
	
	public static List<String> getCityList(String provinceName) throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		
		String cityJson = CityUtil.getCityString();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(cityJson);
		List<String> cityList = new ArrayList<String>();
		
		JsonArray array = object.get("city").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject object2 = array.get(i).getAsJsonObject();
			if(!object2.has("sub")){
				continue;
			}
			if(object2.get("name").getAsString().equals(provinceName)){
				JsonArray array2 = object2.get("sub").getAsJsonArray();
				for (int j = 0; j < array2.size(); j++) {
					String cityStr = array2.get(j).getAsJsonObject().get("name").getAsString();
					if(!(cityStr.equals("请选择")||cityStr.equals("其他"))){
						cityList.add(cityStr);
					}
				}
			}
		}
		return cityList;
	}
	
	/**
	 * 获取某省市县区
	 * @param cityName
	 * @return
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws FileNotFoundException
	 */
	public static List<String> getCountyList(String provinceName,String cityName) throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		String cityJson = CityUtil.getCityString();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(cityJson);
		
		List<String> cityList = new ArrayList<String>();
		
		JsonArray array = object.get("city").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject object2 = array.get(i).getAsJsonObject();
			if(!object2.has("sub")){
				continue;
			}
			if(object2.get("name").getAsString().equals(provinceName)){
				JsonArray array2 = object2.get("sub").getAsJsonArray();
				for (int j = 0; j < array2.size(); j++) {
					JsonObject object3 = array2.get(j).getAsJsonObject();
					if(!object2.has("sub")){
						continue;
					}
					if (object3.get("name").getAsString().equals(cityName)) {
						JsonArray array3 = object3.get("sub").getAsJsonArray();
						for (int k = 0; k < array3.size(); k++) {
							String cityStr = array3.get(k).getAsJsonObject().get("name").getAsString();
							if(!(cityStr.equals("请选择")||cityStr.equals("其他"))){
							cityList.add(cityStr);
							}
						}
						System.out.println("我返回");
						return cityList;
					}
				}
			}
			
		}
		System.out.println("没有值");
		return null;
	}
	
	/**
	 * 获取所有省份
	 * @return
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws FileNotFoundException
	 */
	public static List<String> getProList() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		String cityJson = CityUtil.getCityString();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(cityJson);
		
		List<String> proList = new ArrayList<String>();
		
		JsonArray array = object.get("city").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject object2 = array.get(i).getAsJsonObject();
			String province = object2.get("name").getAsString();
			if (!(province.equals("请选择")||province.equals("其他"))) {
				proList.add(province);
			}
		}
		return proList;
	}
	
	
	/**
	 * 读取本地json文件，获取json格式字符串
	 * @return
	 */
	public static String getCityString(){
		String path = CityUtil.class.getClassLoader().getResource("/ProCityJson.json").getPath().replace("%20", " ");
		File file = new File(path);
        try {
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            String jsonString = sb.toString();
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
}
