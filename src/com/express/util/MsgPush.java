package com.express.util;

import java.util.List;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;




public class MsgPush {
	
	public static void push(String userId,String msg,String masterSecret,String appkey,String orderId,int pushType,String title){
		JPushClient jPushClient = new JPushClient(masterSecret, appkey);
		
		System.out.println(userId+"||"+msg+"||"+masterSecret+"||"+appkey+"||"+orderId+"||"+pushType+"||"+title);
		
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(userId))
				.setNotification(Notification.alert(msg))
				.setMessage(Message.newBuilder().setMsgContent(msg).addExtra("orderId", orderId).addExtra("pushType",pushType).addExtra("title", title).build())
				.build();
		
		try {
			PushResult result = jPushClient.sendPush(payload);
			System.out.println(result.toString());
		} catch (APIConnectionException e) {
			System.out.println("推送失败");
			e.printStackTrace();
		}catch (APIRequestException e) {
			System.out.println("推送失败2");
			//e.printStackTrace();
		}
	}
	
	public static void push(List<String> userIds,String msg,String masterSecret,String appkey,String orderId,int pushType,String title){
		JPushClient jPushClient = new JPushClient(masterSecret, appkey);
		
		
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(userIds))
				.setNotification(Notification.alert(msg))
				.setMessage(Message.newBuilder().setMsgContent(msg).addExtra("orderId", orderId).addExtra("pushType",pushType).addExtra("title", title).build())
				.build();
		
		try {
			PushResult result = jPushClient.sendPush(payload);
			System.out.println(result.toString());
		} catch (APIConnectionException e) {
			System.out.println("推送失败");
			e.printStackTrace();
		}catch (APIRequestException e) {
			System.out.println("推送失败2");
			//e.printStackTrace();
		}
	}
	
//	public static void push(String msg,String masterSecret,String appkey,int pushType,String title){
//		JPushClient jPushClient = new JPushClient(masterSecret, appkey);
//		
//		PushPayload payload = PushPayload.newBuilder()
//				.setPlatform(Platform.all())
//				.setAudience(Audience.all())
//				.setNotification(Notification.alert(msg))
//				.setMessage(Message.content(msg))
//				.build();
//		
//		try {
//			PushResult result = jPushClient.sendPush(payload);
//			System.out.println(result.toString());
//		} catch (APIConnectionException e) {
//			e.printStackTrace();
//		}catch (APIRequestException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void push(String msg,String masterSecret,String appkey,String path,String version){
		JPushClient jPushClient = new JPushClient(masterSecret, appkey);
		
		System.out.println(msg+"||"+masterSecret+"||"+appkey);
		
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setMessage(Message.newBuilder().setMsgContent(msg).addExtra("path", path).addExtra("pushType","4").addExtra("version",version).build())
				.build();
		
		try {
			PushResult result = jPushClient.sendPush(payload);
			System.out.println(result.toString());
		} catch (APIConnectionException e) {
			e.printStackTrace();
		}catch (APIRequestException e) {
			e.printStackTrace();
		}
	}
	
	public static void push(String msg,String masterSecret,String appkey,String title){
		JPushClient jPushClient = new JPushClient(masterSecret, appkey);
		
		System.out.println(msg+"||"+masterSecret+"||"+appkey);
		
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setNotification(Notification.alert(msg))
				.setMessage(Message.newBuilder().setMsgContent(msg).addExtra("pushType","3").addExtra("title",title).build())
				.build();
		
		try {
			PushResult result = jPushClient.sendPush(payload);
			System.out.println(result.toString());
		} catch (APIConnectionException e) {
			e.printStackTrace();
		}catch (APIRequestException e) {
			e.printStackTrace();
		}
	}

}
