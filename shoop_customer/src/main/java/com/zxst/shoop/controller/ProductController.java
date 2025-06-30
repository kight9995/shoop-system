package com.zxst.shoop.controller;

import com.zxst.shoop.entity.Product;
import com.zxst.shoop.service.ProductService;
import com.zxst.shoop.util.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController{

    @Resource
    private ProductService productService;

    @RequestMapping("/getNewProduct")
    public List<Product> getNewProduct(){
       return  productService.getNewProduct();
    }

    @RequestMapping("/getHotProduct")
    public List<Product> getHotProduct(){
       return  productService.getHotProduct();
    }

    //模糊搜索商品信息
    @RequestMapping("/searchGoodsMessage")
    public List<Product> searchGoodsMessage(String info){
       return productService.searchGoodsMessage(info);
    }
    //根据商品的主键获取一个商品信息
    @RequestMapping("/getProDetail")
    public JsonResult getProDetail(Integer pid){
        Product product = productService.getProDetail(pid);
        if (product != null){
            return  new JsonResult(OK,product);
        }else{
            return  new JsonResult(FAIL,null);
        }
    }

}
