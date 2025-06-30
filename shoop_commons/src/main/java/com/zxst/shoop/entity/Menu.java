package com.zxst.shoop.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class Menu implements Serializable {

    private Integer id;

    private String name;

    private String linkUrl;

    private String path;

    private Integer priority;

    private String icon;

    private String description;

    private Integer pid;

    private Integer level;

    //子菜单
    private List<Menu>  children;


}
