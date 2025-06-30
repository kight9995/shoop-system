package com.zxst.shoop.entity;


import lombok.Data;

import java.io.Serializable;


@Data
public class RoleMenu implements Serializable {
	private Integer id;
	
    private Integer roleFk;

    private Integer perFk;


}
