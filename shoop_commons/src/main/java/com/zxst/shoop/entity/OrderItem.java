package com.zxst.shoop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 订单项实体类
 */
@Data
@ToString
@EqualsAndHashCode
public class OrderItem extends BaseEntity{
    private Integer id;
    private String oid;
    private Integer pid;
    private String title;
    private String image;
    private Long price;
    private Integer num;

}
