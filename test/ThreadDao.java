import com.alipay.api.domain.TicketDetailInfo;
import com.express.model.Order;
import com.express.order.service.OrderService;
import com.express.order.service.impl.OrderServiceImpl;


class MythreadDemo implements Runnable{
	Order order = new Order();
	OrderService service = new OrderServiceImpl();
	
	public void run(){
		for (int i = 0; i < 1000; i++) {
			order.setOrderId(String.valueOf(i));
			try {
				service.receive(order);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class ThreadDao {
	
	
	
	public static void main(String[] args) {
		MythreadDemo m = new MythreadDemo();
		Thread t1 = new Thread(m);
		Thread t2 = new Thread(m);
		Thread t3 = new Thread(m);
		t1.start();
		t2.start();
		t3.start();
	}

	
}
