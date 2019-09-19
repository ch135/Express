package com.express.model;

/**
 * Created by Azusa on 2016/7/7.
 */
public class WeChatPayDatas {
    private String partnerId;  //商户ID
    private String prepayId;   //微信返回的支付交易回话Id
    private String noncestr;   //随机字符串
    private String timestamp;   //时间戳
    private String sign;       //签名

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
