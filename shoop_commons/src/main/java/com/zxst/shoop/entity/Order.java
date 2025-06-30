package com.zxst.shoop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 订单的实体类
 */
@Data
@ToString
@EqualsAndHashCode
public class Order extends BaseEntity {
    private String oid;
    private Integer uid;
    private String recvName;
    private String recvPhone;
    private String recvProvince;
    private String recvCity;
    private String recvArea;
    private String recvAddress;
    private Double totalPrice;
    private Integer status;
    private Date orderTime;
    private Date payTime;
   //扩展了关联对象
    private List<OrderItem> orderItemList;

    private User user;
}
