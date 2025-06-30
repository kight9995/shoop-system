package com.zxst.shoop.mapper;

import com.zxst.shoop.entity.Favorite;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface FavoriteMapper {
    boolean addFavoretise(Favorite favorite);

    int dropFavoretise(@Param("fid") Integer fid);
    
    int dropFavoretiseByUidAndPid(@Param("uid") Integer uid, @Param("pid") Integer pid);
    
    List<Favorite> findByUid(@Param("uid") Integer uid);
    
    Favorite findByUidAndPid(@Param("uid") Integer uid, @Param("pid") Integer pid);
}
