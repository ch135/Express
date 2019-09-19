import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.Order;


public class TestBaseD {
	
	public static void main(String[] args){
		BaseDao<Order> dao = new BaseDaoImpl<Order>();
		Order order = new Order();
		order.setOrderId("84147391806118789");
			Order order2 = dao.getOne(order, order.getOrderId());
			System.out.println(order2.getSendName());
	}
}
