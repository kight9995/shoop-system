package com.zxst.shoop.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartVo implements Serializable {
    private Integer cid; //购物车id
    private Integer pid; //商品id
    private Integer uid; //用户id
    private Long price; //商品价格
    private Long totalPrice;//每种商品的小计
    private Integer num;//商品数量
    private String image;//商品的图片
    private String title;//商品的标题
}
