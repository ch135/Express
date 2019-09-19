import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.express.model.User;
import com.express.user.service.UserServiceDAO;
import com.express.user.service.impl.UserServiceImpl;
import com.express.util.CityUtil;
import com.express.util.IdentityCheck;
import com.express.util.MD5Util;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


public class test {

	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
//		List<String> list = CityUtil.getProList();
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
//		List<String> list2 = CityUtil.getCityList("广东");
//		for (int j = 0; j < list2.size(); j++) {
//			System.out.println(list2.size());
//		}
		List<String> list3 = CityUtil.getCountyList("广东", "深圳");
		for (int k = 0; k < list3.size(); k++) {
			System.out.println(list3.size());
		}
	}
}
