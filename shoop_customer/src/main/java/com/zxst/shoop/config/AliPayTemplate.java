package com.zxst.shoop.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zxst.shoop.entity.Order;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AliPayTemplate {

    @Value("{alipay.appId}")
    private String appId;

    @Value("{alipay.privateKey}")
    private String privateKey;

    @Value("{alipay.alipayPublicKey}")
    private String alipayPublicKey;

    @Value("{alipay.notifyUrl}")
    private String notifyUrl;

    @Value("{alipay.returnUrl}")
    private String returnUrl;

    @Value("{alipay.charset}")
    private String charset;

    @Value("{alipay.gatewayUrl}")
    private String gatewayUrl;

    @Value("{alipay.signType}")
    private String signType;

    @Value("{alipay.format}")
    private String format;

    //书写支付过程
    public String pay(Order order)throws AlipayApiException {
        //支付对象
        AlipayClient alipayClient =
                new DefaultAlipayClient(gatewayUrl,appId,privateKey,format,charset,alipayPublicKey,signType);

        //阿里支付的请求对象
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl); //同步回调地址
        alipayRequest.setNotifyUrl(notifyUrl); //异步回调地址


        String oid = order.getOid();
        Double totalPrice = order.getTotalPrice();
        Integer uid = order.getUid();
        String info = uid+"";
        String bodyInfo = uid+"";

        //支付信息设定 订单编号  支付的金额  订单名称   [商品信息   自定义数据 选择设定]   [编号 固定的写法]
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ oid +"\","
                + "\"total_amount\":\""+ totalPrice +"\","
                + "\"subject\":\""+ info +"\","
                + "\"body\":\""+ bodyInfo +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String body = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("支付宝响应的信息"+body);
        return  body;
    }


}
