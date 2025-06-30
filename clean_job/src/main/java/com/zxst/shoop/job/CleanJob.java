package com.zxst.shoop.job;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.zxst.shoop.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


// 定时清理垃圾图片 (oss  mysql中不匹配的图片删除)
@Component
public class CleanJob {

    @Value("${alinfo.oss-endpoint}")
    private String endpoint;
    @Value("${alinfo.oss-accesskeyid}")
    private String accessKeyId;
    @Value("${alinfo.oss-accesskeysecret}")
    private String accessKeySecret;
    @Value("${alinfo.oss-bucketname}")
    private String bucketName;

    @Resource
    private ProductService productService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void task(){
        System.out.println("定时清理oss垃圾图片");
        String path = "https://zxst-shoop-wzh.oss-cn-qingdao.aliyuncs.com/";
        String floderprifex = "admin/";

        // 创建OSS对象
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //获取oss上指定前缀下的所有文件
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            //设定操作图片所在的文件夹的名字
            listObjectsRequest.setPrefix(floderprifex);
            //发送请求获取指定前缀下的文件
            ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
            //盛放oss上存储的文件的名称
            List<String> ossList = new ArrayList<>();
            //将对象中包含的文件信息单独存放到集合
            List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            for (OSSObjectSummary ossObjectSummary : objectSummaries) {
                //将oss上的文件的名称存放到集合中(和数据库完全一致的图片名称)
                ossList.add(path+ossObjectSummary.getKey());
            }
            //数据库中所有的商品图片
            List<String> images = productService.getAllProImage();
            //对比删除
            for (String ossName : ossList) {
                boolean contains = images.contains(ossName);
                if (!contains){
                    String fileName = ossName.substring(ossName.lastIndexOf("/")+1, ossName.length()); // 232ffds.jpg
                    String objectName = floderprifex+fileName; // admin/32423fdfe.jpg
                    ossClient.deleteObject(bucketName,objectName);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            ossClient.shutdown();
        }

    }
}
