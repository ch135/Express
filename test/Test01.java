import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;


public class Test01 {

	public static void main(String[] args) {
		push();

	}
	
	private static void push(){
		String masterSecret = "4ec482a52c01a97d7afa1c83";
		String appkey = "e90b40a9379a1869e06b4c04";
		JPushClient jPushClient = new JPushClient(masterSecret, appkey);
		
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias("402881f55693762f0156937630440000"))
				.setNotification(Notification.alert("又测试了!"))
				.setMessage(Message.content("重新测试"))
				.build();
		
		try {
			PushResult result = jPushClient.sendPush(payload);
			System.out.println("Success");
		} catch (APIConnectionException e) {
			System.out.println("特么的推送失败原因1");
			e.printStackTrace();
		}catch (APIRequestException e) {
			System.out.println("特么的推送失败原因2");
			e.printStackTrace();
		}
				
	}

}
