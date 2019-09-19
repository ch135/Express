import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.express.model.Order;
import com.express.util.HibernateUtil;


public class InsertOrder {
	private static Session session;
	public static void main(String[] args) {
		session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
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
			Long time = System.currentTimeMillis();
//			String orderId =RandomUtil.getRandomNum()+time.toString()+RandomUtil.getRandomNum();
			order.setSendLatitude(23.044855);
			order.setSendLongitude(114.433975);
			order.setOrderId("783241237598347");
			session.save(order);
			
		tx.commit();
		session.close();
	}
}
