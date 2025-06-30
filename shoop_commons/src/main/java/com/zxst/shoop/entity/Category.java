package com.zxst.shoop.entity;

import lombok.Data;

@Data
public class Category extends BaseEntity{
 private String name;
 private Integer id;
 private Integer parentId;
 private Integer status;
 private Integer sortOrder;
 private Integer isParent;


}
