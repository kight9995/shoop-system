package com.zxst.shoop.controller;

import com.zxst.shoop.entity.Favorite;
import com.zxst.shoop.service.FavoriteService;
import com.zxst.shoop.util.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/favoretise")
public class FavoriteController extends BaseController {

    @Resource
    private FavoriteService favoriteService;

    // 添加收藏
    @RequestMapping("/addFavoretise")
    public JsonResult addFavoretise(Integer pid, HttpSession session) {
        Integer userId = getUserId(session);
        String userName = getUserName(session);
        boolean fid = favoriteService.addFavoretise(pid, userId, userName);
        return fid ? new JsonResult(OK) : new JsonResult(FAIL);
    }

    // 按ID删除收藏
    @RequestMapping("/dropFavoretise")
    public JsonResult dropFavoretise(Integer fid) {
        int flag = favoriteService.dropFavoretise(fid);
        return flag > 0 ? new JsonResult(OK) : new JsonResult(FAIL);
    }

    // 按用户+商品删除收藏
    @RequestMapping("/dropByUidAndPid")
    public JsonResult dropByUidAndPid(Integer pid, HttpSession session) {
        Integer uid = getUserId(session);
        int flag = favoriteService.dropFavoretiseByUidAndPid(uid, pid);
        return flag > 0 ? new JsonResult(OK) : new JsonResult(FAIL);
    }

    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    // 获取用户收藏列表
    @RequestMapping("/list")
    public JsonResult list(HttpSession session) {
        try {
            Integer uid = getUserId(session);
            if (uid == null || uid == 0) {
                logger.warn("未获取到用户ID，用户可能未登录");
                return new JsonResult(401, "请先登录");
            }
            
            logger.info("获取用户收藏列表，用户ID: {}", uid);
            List<Favorite> list = favoriteService.getFavoritesByUid(uid);
            logger.info("用户ID: {} 的收藏数量: {}", uid, list != null ? list.size() : 0);
            
            return new JsonResult(OK, list != null ? list : new ArrayList<>());
        } catch (Exception e) {
            logger.error("获取收藏列表异常", e);
            return new JsonResult(500, "系统繁忙，请稍后重试");
        }
    }

    // 检查是否已收藏
    @RequestMapping("/check")
    public JsonResult check(Integer pid, HttpSession session) {
        Integer uid = getUserId(session);
        boolean isFavorite = favoriteService.isFavorite(uid, pid);
        return new JsonResult(OK, isFavorite);
    }
}
