package com.zxst.shoop.service.impl;

import com.zxst.shoop.entity.Favorite;
import com.zxst.shoop.entity.Product;
import com.zxst.shoop.mapper.FavoriteMapper;
import com.zxst.shoop.service.FavoriteService;
import com.zxst.shoop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public boolean addFavoretise(Integer pid, Integer uid, String username) {
        // 检查是否已收藏
        if (isFavorite(uid, pid)) {
            return false;
        }
        
        // 获取商品信息
        Product product = productService.getProDetail(pid);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        // 创建收藏记录
        Favorite favorite = new Favorite();
        favorite.setPid(pid);
        favorite.setUid(uid);
        favorite.setTitle(product.getTitle());
        favorite.setPrice(product.getPrice() != null ? product.getPrice() : 0L);
        favorite.setImage(product.getImage() != null ? product.getImage() + "1.jpg" : "/images/default-product.jpg");
        favorite.setCreateUser(username);
        favorite.setCreateTime(new Date());
        
        return favoriteMapper.addFavoretise(favorite);
    }

    @Override
    public int dropFavoretise(Integer fid) {
        return favoriteMapper.dropFavoretise(fid);
    }

    @Override
    public int dropFavoretiseByUidAndPid(Integer uid, Integer pid) {
        return favoriteMapper.dropFavoretiseByUidAndPid(uid, pid);
    }

    @Override
    public List<Favorite> getFavoritesByUid(Integer uid) {
        return favoriteMapper.findByUid(uid);
    }

    @Override
    public boolean isFavorite(Integer uid, Integer pid) {
        return favoriteMapper.findByUidAndPid(uid, pid) != null;
    }
}
