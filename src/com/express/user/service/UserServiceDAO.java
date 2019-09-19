package com.express.user.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URIException;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;

import com.express.model.Coupon;
import com.express.model.Expense;
import com.express.model.InCash;
import com.express.model.Order;
import com.express.model.ShareCoupon;
import com.express.model.ShareWeiXin;
import com.express.model.User;
import com.express.model.UserAdvice;
import com.express.model.UserIDcard;

public interface UserServiceDAO {

	public boolean insertUser(User user);   //用户注册，插入数据
	
	public User findUserByPhone(String mobile); //通过手机号查找用户
	//上传用户认证信息
	public void idAuth(UserIDcard userTemp); 
	
	public void updataUser(User user);//用户完善信息，暂时用不到（未完成）
	
	public boolean findUser(String mobile,String pwd);//检查手机号密码是否正确
	
	public void reLoginPwd(String mobile,String pwd);//修改登录密码
	
	public void changeIcon(User user);//用户修改头像
	
	public void saveAdvice(UserAdvice userAdvice);//用户建议
	
	public User checkUser(User user) throws NoSuchFieldException;//检查用户
	
	public void rePayPwd(User user) throws NoSuchFieldException;//修改支付密码
	
	public void rePayPwdByPhone(User user) throws NoSuchFieldException;//通过手机号修改密码

	public List<Expense>findExpenses(Expense expense,int first) throws NoSuchFieldException;//查找明细
	
	public List<Order> getOrder(String id,int first);//用户获取订单
	
	public User getUser(String userId);//通过id获取用户

	public List<Order> getTwoOrder(String userId);

	public String getCover();

	public List<Coupon> getCoupon(String userId);//获取优惠券

	public void userAddCoupon(String userId);

	public void userGetCoupon(String mobile, double value);

	public ShareWeiXin getShareWeiXin();//获取分享内容

	public ShareCoupon checkShare(String mobile, String orderId);

	public void insertShareCoupon(String orderId, String mobile, double value);

	User findUnionIdByPhone(String mobile);
	
	public void deleteUser(String userId);

	List<InCash> findInCashByDate(Date date);
	
}