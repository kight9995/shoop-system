package com.zxst.shoop.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 向客户端提供数据封装工具类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult implements Serializable {
    private Long total;//数据的总条数
    private List rows;//本次展示的数据

}
