package com.zxst.shoop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
public class Role implements Serializable {

    private Integer roleid;

    private String rolename;

    private String keyword;

    private String description;

    private Integer status;


}
