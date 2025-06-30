package com.zxst.shoop.controller;

import com.zxst.shoop.entity.Product;
import com.zxst.shoop.service.ProductService;
import com.zxst.shoop.util.JsonResult;
import com.zxst.shoop.util.PageResult;
import com.zxst.shoop.util.QueryPageBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return  productService.findPage(queryPageBean);
    }

    @RequestMapping("/downProduct")
    public JsonResult downProduct(Integer id){
        Product proDetail = productService.getProDetail(id);
        if (proDetail.getStatus() == 1){
            proDetail.setStatus(0);
        }else{
            proDetail.setStatus(1);
        }
        boolean flag = productService.updateInfo(proDetail)>0;
        if (flag){
            return new JsonResult(200);
        }else{
            return new JsonResult(500);
        }
    }

    //添加商品
    @RequestMapping("/saveProduct")
    public JsonResult saveProduct(@RequestBody Product product, HttpSession session){
        boolean flag = productService.saveProduct(product)>0;
        if (flag){
            return new JsonResult(200);
        }else {
            return new JsonResult(500);
        }
    }

}
