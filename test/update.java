import com.express.admin.service.AdminService;
import com.express.admin.service.impl.AdminServiceImpl;
import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.User;


public class update {
	public static void main(String[] args) throws NoSuchFieldException {
		BaseDao<User> dao = new BaseDaoImpl<User>();
		User user = new User();
		user.setId("297eec7d5654edf9015654edfa670000");
		user.setBalance(10000);
		user.setJifen(250);
		dao.updates(user);
	}
}
