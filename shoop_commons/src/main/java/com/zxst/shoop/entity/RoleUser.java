package com.zxst.shoop.entity;

import lombok.Data;
import java.io.Serializable;


@Data

public class RoleUser implements Serializable {

    private Integer userId;

    private Integer roleId;


}
