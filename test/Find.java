import com.express.database.dao.BaseDao;
import com.express.database.dao.impl.BaseDaoImpl;
import com.express.model.User;


public class Find {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		BaseDao<User>dao = new BaseDaoImpl<User>();
		User user = new User();
		user = dao.getOne( user, "297eec7d5654edf9015654edfa670000");
		System.out.println(user.getAccept_able());

	}

}

