package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.Product;
import com.zxst.shoop.mapper.OrderItemMapper;
import com.zxst.shoop.mapper.OrderMapper;
import com.zxst.shoop.mapper.ProductMapper;
import com.zxst.shoop.mapper.UserMapper;
import com.zxst.shoop.service.ReportService;
import com.zxst.shoop.util.DateUtils;
import com.zxst.shoop.util.JsonResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;



@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public JsonResult getCustomerReport() {
        Map<String,Object> map = new HashMap<>();
        List<String> month = new ArrayList<>();
        List<Integer> mcount =  new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();//jdk 日历工具类
        calendar.add(Calendar.MONTH, -12); // 2024-05-22
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date date = calendar.getTime();
            String format = sdf.format(date); // 2024-05
            month.add(format);
            //当前月份的注册会员人数
            String  start = format+"-01";

            String end = format+"-31";
            int memberCt = userMapper.getCtByDate(start,end);
            mcount.add(memberCt);
        }
        map.put("monthes",month);
        map.put("customCt",mcount);
        return new JsonResult(200,map);
    }

    @Override
    public JsonResult getOrderReport() {
        Map<String,Object> map = new HashMap<>();
        //盛放商品名称
        List<String> proName = new ArrayList<>();
        //商品名称 每种商品的销售数量
        List<Map<String,Object>> mapList = new ArrayList<>();
        //获取被购买的商品信息 t_order_item表
        List<Product> products = productMapper.getOrderProductInfo();
        for (Product product : products) {
            //将商品的名称描述放入到集合中
            proName.add(product.getTitle());
            //获取商品的销售数量
            Integer proCount = orderItemMapper.getCountByPid(product.getId());
            Map<String,Object> smallMap = new HashMap<>();
            smallMap.put("name",product.getTitle());
            smallMap.put("value",proCount);
            mapList.add(smallMap);
        }
        map.put("productNames",proName);
        map.put("productCount",mapList);
        return new JsonResult(200,null,map);
    }

    @Override
    public JsonResult getBusinessReport() {
        Map<String,Object> map = new HashMap<>();
        //注意将t_user表中的日期类型修改为 date
        try {
            //获取今天日期
            Date today = DateUtils.getToday();
            String todayStr = DateUtils.parseDate2String(today, "yyyy-MM-dd");
            map.put("reportDate",todayStr);
            //今日所在周的第一天日期
            Date firstDayOfWeek = DateUtils.getFirstDayOfWeek(today);
            String firstDayOfWeekStr = DateUtils.parseDate2String(firstDayOfWeek, "yyyy-MM-dd");
            //今日所在月的第一天日期
            Date firstDateOfMonth = DateUtils.getFirstDay4ThisMonth();
            //今天新增会员数
            Integer todayNewMember = userMapper.getTodayNewMember(todayStr);
            map.put("todayNewMember",todayNewMember);
            //本周新增会员数
            Integer weekCount = userMapper.getNewMemberByTime(firstDayOfWeek, today);
            map.put("thisWeekNewMember",weekCount);
            //本月新增会员数
            Integer monthCount = userMapper.getNewMemberByTime(firstDateOfMonth, today);
            map.put("thisMonthNewMember",monthCount);
            //总会员数
            Integer totalMember = userMapper.getTotalMember();
            map.put("totalMember",totalMember);
            //今日订单
            Integer oct = orderMapper.getTodayOrder(todayStr);
            map.put("todayOrderNumber",oct);
            //本周订单
            Integer weekOrder = orderMapper.getWeekOrder(firstDayOfWeekStr, todayStr);
            map.put("todayVisitsNumber",weekOrder);

            //热门商品
            List<Map<String, Object>> hotProduct = orderItemMapper.getHotProduct();
            map.put("hotSetmeal",hotProduct);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new JsonResult(200,null,map);
    }
}
