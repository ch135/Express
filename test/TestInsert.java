
import java.util.Date;

import org.junit.Test;

import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.Order;
import com.express.model.User;
import com.express.util.MD5Util;
import com.express.util.RandomUtil;


public class TestInsert {

	@Test
	public void insert(){
		User user = new User();
		user.setName("陈德仁");
		user.setMobile("15119087276");
		user.setPath("/Express/images/identity/157679795881.jpg");
		user.setLoginPassword(MD5Util.LoginEncryption("123456"));
		user.setState("1");
		user.setPayPassword(MD5Util.LoginEncryption("123456"));
		user.setBalance(10000.00);
		user.setSex("男");
		user.setAddress("惠州");
		user.setLatitude(0);
		user.setLongitude(0);
		user.setSend_able(true);
		user.setAccept_able(true);
		System.out.println("放进去的密码是:"+user.getLoginPassword());
		BaseDao<User> dao = new BaseDaoImpl<User>();
		dao.save(user);
	}
	@Test
	public void insertOrder(){
		BaseDao<Order> dao = new BaseDaoImpl<Order>();
		Order order = new Order();
		order.setUserid("4028d88156b2eb180156b2eb19df0000");
		order.setCourierid("4028d88156b2eb180156b2eb19df0000");
		order.setRequestDate(new Date());
		order.setSendName("张三");
		order.setSendAddress("惠州市 惠城区 X200(南岸路) 在西湖苑附近");
		order.setSendMobile("15767978870");
		order.setReceiveName("彬松");
		order.setReceiveAddress("西湖苑 哈哈哈哈");
		order.setReceiveLongitude(114.433988);
		order.setReceiveLatitude(23.044833);
		order.setGoodsDetail("哈哈哈哈");
		order.setOrderFare(3.33);
		order.setOrderStaus(1);
		order.setReceiveMobile("15767979588");;
		order.setGoodsPic("/Express/images/identity/157679795881.jpg,/Express/images/identity/157679795881.jpg,/Express/images/identity/157679795881.jpg,/Express/images/identity/157679795881.jpg,/Express/images/identity/157679795881.jpg");
		order.setCgrade(2.5f);
		order.setCname("彬松");
		order.setCpath("/Express/images/icon/334455.jpg");
		order.setCmobile("15767979588");
		for (double i = 0.0 ; i < 5; i++) {
			Long time = System.currentTimeMillis();
			String orderId =RandomUtil.getRandomNum()+time.toString()+RandomUtil.getRandomNum();
			order.setOrderId(orderId);
			order.setSendLatitude(23.044855);
			order.setSendLongitude(114.433975);
			dao.save(order);
			
		}
	}

//	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
//		Random random = new Random();
//		TestInsert testInsert = new TestInsert();
//		List<String> list = CityUtil.getCityList("广东");
//		for (int i = 0; i < 10000; i++) {
//			int randomNumber =  (random.nextInt(21));
//			testInsert.insert(list.get(randomNumber));
//		}
//		
//	}
}
