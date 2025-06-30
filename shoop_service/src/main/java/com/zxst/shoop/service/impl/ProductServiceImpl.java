package com.zxst.shoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxst.shoop.entity.Product;
import com.zxst.shoop.mapper.ProductMapper;
import com.zxst.shoop.service.ProductService;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public List<Product> getNewProduct() {
        return productMapper.getNewProduct();
    }

    @Override
    public List<Product> getHotProduct() {
        return productMapper.getHotProduct();
    }

    @Override
    public List<Product> searchGoodsMessage(String info) {
        return productMapper.searchGoodsMessage(info);
    }

    @Override
    public Product getProDetail(Integer pid) {
        return productMapper.getInfoById(pid);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<Product> list = productMapper.findPage(queryPageBean.getQueryString());
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public int updateInfo(Product proDetail) {
        return productMapper.updateInfo(proDetail);
    }

    @Override
    public int saveProduct(Product product) {
        product.setCreatedTime(new java.util.Date());
        product.setModifiedTime(new java.util.Date());
        product.setCreatedUser("admin");
        product.setModifiedUser("admin");
        Random random = new Random();
        int i = random.nextInt(999999);
        int id = Integer.parseInt(String.format("%06d",i));
        product.setId(id);
        return productMapper.saveInfo(product);
    }

    @Override
    public List<String> getAllProImage() {
        return productMapper.getAllProImage();
    }
}
