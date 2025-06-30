package com.zxst.shoop.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zxst.shoop.entity.Banner;
import com.zxst.shoop.service.BannerService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController {

    @Resource
    private BannerService bannerService;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    //测试阿里 oss中的文件上传
    @RequestMapping("/upload")
    public String upload(MultipartFile files) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-qingdao.aliyuncs.com";
        //OSS_ACCESS_KEY_ID
        String accessKeyId = "LTAI5tNabZwaUbviMbzmdcE8";
        //和OSS_ACCESS_KEY_SECRET
        String accessKeySecret = "k83ebE2XVBR02DCDInHNSumjfojYHr";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "zxst-shoop-new";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String filename = files.getOriginalFilename();
        String objectName = "exampledir/"+filename;
        // 创建OSS对象
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //上传
            ossClient.putObject(bucketName, objectName, files.getInputStream());
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        //构建path  https://zxst-shoop-new.oss-cn-qingdao.aliyuncs.com/admin.jpg
        String path = "https://zxst-shoop-new.oss-cn-qingdao.aliyuncs.com/" + objectName;
        return  path;
    }





    /**
     * 使用缓存的方式解决加载首页banner图问题
     * @return
     */
    @RequestMapping("/showBanner")
    public List<String> showBanner(){
        SetOperations<String, String> sop = redisTemplate.opsForSet();
        List<String> list = new ArrayList<>();
        //判断redis中是否存在banner信息  key(banner)/value("pic1.jpg","pic2.jpg",...)
        if (redisTemplate.hasKey("banner")) {
            //获取redis中key是banner信息
            Set<String> banner = sop.members("banner");
            //将set集合中的元素添加到list集合中
            banner.forEach(b -> list.add(b));
        }else{
            //如果不存在从数据库中取,在redis中存放一份
            List<Banner> banners = bannerService.showBanner();
            for (Banner banner : banners) {
                list.add(banner.getBimg());
                sop.add("banner",banner.getBimg());
            }
        }
        return list;
    }
}
