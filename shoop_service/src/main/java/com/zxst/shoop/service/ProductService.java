package com.zxst.shoop.service;

import com.zxst.shoop.entity.Product;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;

import java.util.List;

public interface ProductService {
    List<Product> getNewProduct();

    List<Product> getHotProduct();

    List<Product> searchGoodsMessage(String info);

    Product getProDetail(Integer pid);

    PageResult findPage(QueryPageBean queryPageBean);

    int updateInfo(Product proDetail);

    int saveProduct(Product product);

    List<String> getAllProImage();
}
