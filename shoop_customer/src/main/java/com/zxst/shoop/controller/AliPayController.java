package com.zxst.shoop.controller;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.zxst.shoop.config.AliPayTemplate;
import com.zxst.shoop.entity.Order;
import com.zxst.shoop.entity.User;
import com.zxst.shoop.service.PayService;
import com.zxst.shoop.service.UserService;
import com.zxst.shoop.util.JsonResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/alipay")
public class AliPayController extends BaseController {

    @Resource
    private AliPayTemplate alipayTemplate;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private PayService payService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/pay",produces = "text/html;charset=utf-8")
    public String pay(String oid, Double totalPrice, HttpSession session){
        Order order = new Order();
        order.setOid(oid);
        order.setTotalPrice(totalPrice);
        order.setUid(getUserId(session));
        String pay = null;
        try {
            pay = alipayTemplate.pay(order);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        return pay;
    }

    //阿里服务器完成支付后默认回调的方法
    @RequestMapping("/notify")
    public String notify(HttpServletRequest request) throws AlipayApiException {
        //阿里的服务器 是否能够将我们提交的数据返回
        String tradeStatus = request.getParameter("trade_status");
        //TRADE_SUCCESS  TRADE_ERROR
        // ..../show?id=123&name=admin
        Map<String,String> map = new HashMap<>();
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            //获取阿里服务器发送的请求中所有的数据名称  值
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<String> keySet = parameterMap.keySet(); // [id,name]

            for (String key : keySet) {

                String value = request.getParameter(key);
                map.put(key, value);
            }
            //支付包验签
            boolean flag = AlipaySignature.rsaCheckV1(map
                    , alipayTemplate.getAlipayPublicKey()
                    , alipayTemplate.getCharset()
                    , alipayTemplate.getSignType());
            if (flag) {
                //验证签名通过
                System.out.println("交易名称:"+map.get("subject"));
                System.out.println("订单编号:"+map.get("out_trade_no"));
                System.out.println("支付金额:"+map.get("total_amount"));
                System.out.println("商品信息:"+map.get("body"));
                System.out.println("买家id:"+map.get("buyer_id"));
                System.out.println("支付宝交易凭证编号:"+map.get("trade_no"));
                System.out.println("交易状态:"+map.get("trade_status"));
            }
        }

        int uid = Integer.parseInt(map.get("subject"));
        User user = userService.showOneUser(uid);
        //暂时使用模拟的用户名完成后续任务
        boolean flag  = payService.saveOrder(user.getUsername());
        return "success";
    }
}
