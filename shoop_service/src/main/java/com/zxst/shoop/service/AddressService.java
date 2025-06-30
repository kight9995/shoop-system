package com.zxst.shoop.service;

import com.zxst.shoop.entity.Address;

import java.util.List;

public interface AddressService {
    List<Address> showAddress(Integer userId);

    boolean setDefaultAddr(Integer aid, Integer userId);

    boolean saveAddress(Integer userId, String userName, Address address);

    Address getAddressByAid(Integer sendAddress);
}
