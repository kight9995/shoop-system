package com.zxst.shoop.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页工具类
 * 封装客户端传递的分页信息
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryPageBean implements Serializable {
    //当前页
    private Integer currentPage;
    //每页展示的条数
    private Integer pageSize;
    //搜索条件
    private String queryString;
}
