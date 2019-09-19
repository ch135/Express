import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.User;


public class Delete {

	public static void main(String[] args) {
		BaseDao<User> dao =new BaseDaoImpl<User>();
		User user = new User();
		user.setId("297eec7d5654ea7a015654ea7be60000");
		dao.delete(user);

	}

}
