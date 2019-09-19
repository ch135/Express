package com.express.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088911204467585";
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN/CuZc5fnHKjsXK1K0+M3g/qvyK9L/SltoLsnXC9Rs43fC3H/ZM9BWEikCz93d6q609Z5GD6gCqxfhtPCrJDyRCLYsIoVEqFWTfouuSzK5ouHpvZfknBpGswdF/6vk3aVIhv135WBTaviQi3XtbH7fOGNPd7Tw2WRhJuiwQcjGlAgMBAAECgYA1onpT+k8JvZDdblHuONOjhjdZE4G5HSRVnaooOhQAc3AXi/2YTzfOlJeDDOBLflb6TpattZ34jPapJp3eb5U2G/1ccOsAtzDVZ7kRWXWVhHZKHLIo16xDTZYS9EvjNzRscT33VtC6nb38m4TIjipXRdCfN3cNOBuRbOagD4gpHQJBAPBjEK6vXunhmzwVXgbZEY87VMFKH5uy0djkYbtKmEXe2xSH+rvdDDUlBImVKQDpj0vicPzRzPQOMtGeryK0xR8CQQDuSzZf4tRHUJszjnKyrER5NoJfzNo3ErX4gmmV8J0f5MkPfb754c7eVRSO0FixSOyumwQU8Q2G4zXFJ+gO/0y7AkEA5suWVsFVdWQAMP2q2xRKAaTLSRErq8dSJtWO581EycCfwuSTXQ9sCxJReu7VAWU9CXSNWo7sIl4HeK7MI5w/rQJAKqsvDlZYeniVgdLU+Oddz3KSP8M3o7rm+Jupel6+U9KsqehzRN1JJIiJiNUHvJ75On6paXKMYIXYv/fYVq8iNwJBAMJbi5HK6oGAXRrJ7VSfPHd5DD5riI8ykGjRcbFt8j6QKEnwgcUQkj3DkmnnledUuVJQqNbLRCNhZZDHr9xjgUc=";

	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://WWW.nmbzz.cn/user/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://WWW.nmbzz.cn/user/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "D:\\";
		
	// 字符编码格式 目前支持utf-8
	public static String input_charset = "utf-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// 调用的接口名，无需修改
	public static String service = "alipay.wap.create.direct.pay.by.user";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}

