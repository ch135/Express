package com.express.util;

/**
 * @ClassName Constant
 * @Description ��ų����Ĺ�����
 * @author jackson
 * @date 2016��3��24������11:26:52
 */
public final class Constant {

	// 日期格式
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMMDDSS = "yyyyMMddss";
	public static final String HH_MM = "HH:mm";
	public static final String HH_MM_SS_MMM = "HH:mm:ss.SSS";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS_MMM = "yyyy-MM-dd HH:mm:ss.SSS";

	// 分页
	public static final byte PAGE = 15;

	// 应用的常量，用于加密
	public static final String WEBKEY = "EXPRESS";
	// cookie的有效期
	public static final long COOKIEMAXAGE = 3600 * 24 * 30;
	// 存储cookie的名称
	public static final String COOKIEDOMAINNAME = "express";

	// 用户默认头像的绝对路径
	public static final String PATH_DEFAULT_ICON = "/Express/default/user_logo.png";
	// 存放身份证图片的绝对路径
	public static final String PATH_PREFIX = "/Express/images/identity/";
	// 存放头像图片的绝对路径
	public static final String PATH_ICON = "/Express/images/icon/";
	// 存放物品图片的绝对路径
	public static final String PATH_GPIC = "/Express/images/goodspic/";
	// 存放更新同城快递app的绝对路径
	public static final String PATH_UAPK = "/Express/UploadAPK/CityExpressForUser.apk";
	// 存放更新同城快递员app的绝对路径
	public static final String PATH_CAPK = "/Express/UploadAPK/CityExpress.apk";
	// 存放APP封面图片的绝对路径
	public static final String PATH_COVER = "/Express/images/cover/";

	// 信息模版
	public static String MSG = "【快快送】";

	// 推送的appkey
	public static final String JPUSH_CAPPKEY = "3c2ef837e050b3c13db0384c";
	public static final String JPUSH_UAPPKEY = "e90b40a9379a1869e06b4c04";
	// 推送的masterSecret
	public static final String JPUSH_CCMASTER_SECRET = "ef702f4c5fa350aa6537d4f0";
	public static final String JPUSH_UCMASTER_SECRET = "4ec482a52c01a97d7afa1c83";

	// 短信接口的apikey -------15767979588
	public static final String MSGAPIKEY = "8c86a95a0c850d2ee2eaf851262fa5ad";
	// 身份证验证接口的apikey
	public static final String IDAPIKEY = "d6987b4dfc9197869e8a20dc701703fe";

	// 支付宝接口参数
	// 正式应用APPID
	public static final String PID = "2088911204467585";// 拓嵘账号
	public static final String APP_ID = "2016091301896624";
	public static final String APP_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN/CuZc5fnHKjsXK1K0+M3g/qvyK9L/SltoLsnXC9Rs43fC3H/ZM9BWEikCz93d6q609Z5GD6gCqxfhtPCrJDyRCLYsIoVEqFWTfouuSzK5ouHpvZfknBpGswdF/6vk3aVIhv135WBTaviQi3XtbH7fOGNPd7Tw2WRhJuiwQcjGlAgMBAAECgYA1onpT+k8JvZDdblHuONOjhjdZE4G5HSRVnaooOhQAc3AXi/2YTzfOlJeDDOBLflb6TpattZ34jPapJp3eb5U2G/1ccOsAtzDVZ7kRWXWVhHZKHLIo16xDTZYS9EvjNzRscT33VtC6nb38m4TIjipXRdCfN3cNOBuRbOagD4gpHQJBAPBjEK6vXunhmzwVXgbZEY87VMFKH5uy0djkYbtKmEXe2xSH+rvdDDUlBImVKQDpj0vicPzRzPQOMtGeryK0xR8CQQDuSzZf4tRHUJszjnKyrER5NoJfzNo3ErX4gmmV8J0f5MkPfb754c7eVRSO0FixSOyumwQU8Q2G4zXFJ+gO/0y7AkEA5suWVsFVdWQAMP2q2xRKAaTLSRErq8dSJtWO581EycCfwuSTXQ9sCxJReu7VAWU9CXSNWo7sIl4HeK7MI5w/rQJAKqsvDlZYeniVgdLU+Oddz3KSP8M3o7rm+Jupel6+U9KsqehzRN1JJIiJiNUHvJ75On6paXKMYIXYv/fYVq8iNwJBAMJbi5HK6oGAXRrJ7VSfPHd5DD5riI8ykGjRcbFt8j6QKEnwgcUQkj3DkmnnledUuVJQqNbLRCNhZZDHr9xjgUc=";
	// 正式应用公钥
	public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
	public static final String ALIPAY_ADD_NOTIFY_URY = "http://112.74.84.207/Express/receive/Receive_aliPayNotify";
	public static final String ALIPAY_PAY_NOTIFY_URY = "http://112.74.84.207/Express/receive/Receive_notifyCheck";

	// 微信接口参数
	// 同城快递商户号以及私钥
	public static final String WEIXIN_APP_ID_USER = "wx793301d6b26c62e1";
	public static final String WEIXIN_PID_USER = "1395691002";
	public static final String WEIXIN_SECRET_USER = "d6a69e5e17f92aefa8e977676e74c246";
	public static final String WEIXIN_KEY_USER = "Kfh1vA5Bro70mAmULb1SSZAv9WQNTrk1";
	public static final String WEIXIN_GZH_APPID = "wx43e658851c7bef06";
	public static final String WEIXIN_SECRET = "c413fc84ab26a9b24ca5f23b36b70f6c";

	// public static final String WEIXIN_KEY_PUSER=
	// "b561c46223070b0e3876ea4781cd1806";
	// 同城快递人商户号及私钥
	public static final String WEIXIN_APP_ID_CUSER = "wxe22e492ddc4544d3";
	public static final String WEIXIN_PID_CUSER = "1395685702";
	public static final String WEIXIN_SECRET_CURSR = "34b03f26cd71a85c3fcb4cfeafbc6690";
	public static final String WEIXIN_KEY_CUSER = "NWzGYDO5DhPkR5W7lanrCyQUEJYjx0I5";
	public static final String WEIXIN_ADD_NOTIFY_URY = "http://112.74.84.207/Express/receive/Receive_wxNotify";
	public static final String WEIXIN_ADD_CNOTIFY_URY = "http://112.74.84.207/Express/receive/Receive_wxcNotify";
	// public static final String WEIXIN_PAY_NOTIFY_URY =
	// "http://112.74.84.207/Express/receive/Receive_wXnotifyCheck";
	public static final String WEIXIN_PAY_NOTIFY_URY = "http://112.74.84.207/Express/receive/Receive_wXnotifyCheck";

	// 微信快递公众号
	// 同城快递人商户号及私钥
	public static final String WEIXIN_WEB_ID_USER = "wxbe953834adba0aee";// 小程序ID
	public static final String WEIXIN_WEB_PID_USER = "1395691002"; // 商户号
	public static final String WEIXIN_WEB_KEY_USER = "Kfh1vA5Bro70mAmULb1SSZAv9WQNTrk1";// 秘钥
	public static final String WEIXIN_WEB_APPID_USER = "7489ced650bb1a874ba954df1cef9211";//
	public static final String WEIXIN_WEB_NOTIFY_USER = "http://112.74.84.207/Express/receive/Receive_wXPublicNotifyCheck";

	// 百度地图距离计算url
	public static final String BAIDU_MAP_URL = "http://api.map.baidu.com/routematrix/v2/driving";
	public static final String BAIDU_MAP_AK = "FUgUj6RqZQ8ly716WGSzzIjRu86Pomry";
	public static final String BAIDU_MAP_MCODE = "E7:12:35:0E:5D:CC:73:3E:B3:22:DE:8C:AA:60:CD:B8:20:32:45:FB;com.waibao.team.cityexpressforsend";

	// 分享红包url
	public static final String SHARE_COUPON_URL = "http://112.74.84.207/Express/user/User_toSharePage";

	// 手续费
	public static double TOLL;
	// 分享红包上下限
	public static double MIN = -1;
	public static double MAX = -1;
}
