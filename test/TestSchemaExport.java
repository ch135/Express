
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import com.express.model.Admin;
import com.express.user.service.UserServiceDAO;
import com.express.user.service.impl.UserServiceImpl;
import com.express.util.HibernateUtil;
import com.express.util.MD5Util;
import com.express.util.MsgUtil;


public class TestSchemaExport {

	@Test
	public void testSchemaExport()
	{
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure().build();

		MetadataImplementor metadataImplementor = (MetadataImplementor)
		new MetadataSources(serviceRegistry).buildMetadata();

		SchemaExport export = new SchemaExport(serviceRegistry, metadataImplementor);
		export.create(true, true);
	}
	
	@Test
	public void testSave()
	{
		Transaction transaction=null;
		
		Session session= HibernateUtil.getSessionFactory().getCurrentSession();
		
		transaction = session.beginTransaction();
		
		Admin admin = new Admin();
		admin.setId(MD5Util.getUUID());
		admin.setUsername("admin");
		admin.setPassword(MD5Util.LoginEncryption("admin"));
		admin.setRole("admin");
		
		session.save(admin);
		
		transaction.commit();
		
	}
	
	@Test
	public void getMsg(){
		UserServiceDAO mes = new UserServiceImpl();
		System.out.println(MsgUtil.msg("15767978870","您的验证码是："));
		
	}
}
