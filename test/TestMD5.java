import com.express.util.MD5Util;


public class TestMD5 {

	public static void main(String[] args) {
		String pass="admin";
		System.out.println(MD5Util.LoginEncryption(pass));
		
	}
}
