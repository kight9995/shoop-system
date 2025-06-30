package com.zxst.shoop.service;

import com.zxst.shoop.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    boolean addFavoretise(Integer pid, Integer uid, String username);

    int dropFavoretise(Integer fid);

    int dropFavoretiseByUidAndPid(Integer uid, Integer pid);

    List<Favorite> getFavoritesByUid(Integer uid);

    boolean isFavorite(Integer uid, Integer pid);
}
