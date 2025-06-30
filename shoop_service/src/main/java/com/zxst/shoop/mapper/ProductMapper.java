package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface ProductMapper {
    List<Product> getNewProduct();

    List<Product> getHotProduct();

    List<Product> searchGoodsMessage(@Param("info") String info);

    Product getInfoById(@Param("id") Integer pid);

    int updateInfo(Product infoById);

    List<Product> findPage(@Param("search") String queryString);

    int saveInfo(Product product);

    List<Product> getOrderProductInfo();

    @Select("select image from t_product")
    List<String> getAllProImage();
}
