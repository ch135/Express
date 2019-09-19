package com.express.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;





import Decoder.BASE64Encoder;


/**
 * @ClassName MD5Util
 * @Description 用于加密的工具类
 * @author jackson
 * @date 
 */
public final class MD5Util {

	/**
	 * 生成UUID作为表的主键
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 *通过md5算法以及base64对密码进行加密，并经过64编码转成字符串
	 * @param str
	 * @return
	 */
	public static String md5AndBase64(String str){
		try {
			if(str==null || "".equals(str)){
				return null;
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(str.getBytes());
			BASE64Encoder be = new BASE64Encoder();
			return be.encode(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static String getMD5(String str) {
		try {
			if(str==null || "".equals(str)){
				return null;
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(str.getBytes());
			return toHex(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static String getPayWordMD5(String str) {
		try {
			if(str==null || "".equals(str)){
				return null;
			}
			char temp;
			char[] pwd = str.toCharArray();
			temp = pwd[0];
			pwd[0] = pwd[15];
			pwd[15] = temp;
			pwd[3] = 'a';
			pwd[7] = 'b';
			pwd[10] = 'c';
			str = String.valueOf(pwd);
			System.err.println(str);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(str.getBytes());
			return toHex(bytes).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 转成16进制
	 * @param buffer
	 * @return
	 */
	private static String toHex(byte[] buffer){
		StringBuffer sb = new StringBuffer(buffer.length * 2); 
		for (int i = 0; i <buffer.length; i++){
			sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16)); 
			sb.append(Character.forDigit(buffer[i] & 0x0f, 16)); 
		}
		return sb.toString(); 
	}
	
	/**
	 * 用于对登录密码的加密
	 * @param pwd
	 * @return
	 */
	public static String LoginEncryption(String pwd)
	{
		try {
			if(pwd==null || "".equals(pwd)){
				return null;
			}
			MessageDigest md = MessageDigest.getInstance("SHA");
			pwd = toHex(md.digest(pwd.getBytes()));
			return pwd;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	
}
