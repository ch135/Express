import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class GetCityList {

	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Long date = System.currentTimeMillis();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(new FileReader("test/ProCityJson.json"));
		
		List<String> cityList = new ArrayList<String>();
		
		JsonArray array = object.get("city").getAsJsonArray();
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject object2 = array.get(i).getAsJsonObject();
//			System.out.println(object2.get("name").getAsString());//全国省份
			if(!object2.has("sub")){
				continue;
			}
			if(object2.get("name").getAsString().equals("广东")){
				JsonArray array2 = object2.get("sub").getAsJsonArray();
				for (int j = 0; j < array2.size(); j++) {
					String cityStr = array2.get(j).getAsJsonObject().get("name").getAsString();
					if(!(cityStr.equals("请选择")||cityStr.equals("其他"))){
						cityList.add(cityStr);
					}
					
				}
			}
			
		}
		System.out.println(System.currentTimeMillis()-date);
		System.out.println(cityList.size());
		for (int i = 0; i < cityList.size(); i++) {
			System.out.println(cityList.get(i));
		}
	}

}
