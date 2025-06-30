package com.zxst.shoop.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zxst.shoop.entity.User;
import com.zxst.shoop.util.JsonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
@RequestMapping("/uploadpic")
public class ManagementFileController extends BaseController {

    @Value("${alinfo.oss-endpoint}")
    private String endpoint;
    @Value("${alinfo.oss-accesskeyid}")
    private String accessKeyId;
    @Value("${alinfo.oss-accesskeysecret}")
    private String accessKeySecret;
    @Value("${alinfo.oss-bucketname}")
    private String bucketName;

    @RequestMapping("/saveProductPic")
    public JsonResult savePic(@RequestParam("imgFile") MultipartFile file, HttpSession session) {
        System.out.println(endpoint+"\t"+accessKeyId+"\t"+accessKeySecret+"\t"+bucketName+"\t"+file.getOriginalFilename());
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String filename = file.getOriginalFilename(); // 4.5.6.7.82.jpg
        String uuidStr = UUID.randomUUID().toString().replace("-",""); // uuid字符串
        System.out.println(uuidStr);
        filename = filename.substring(filename.lastIndexOf("."));
        String newFileName = uuidStr + filename;

        String userName = getUserName(session);
        if (userName == null || "".equals(userName)) {
            userName = "admin";
        }
        String objectName = userName+"/"+newFileName;
        // 创建OSS对象
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //上传前检查bucket是否存在
            if (!ossClient.doesBucketExist(bucketName)) {
                return new JsonResult(500, "Bucket不存在", null);
            }
            //上传
            ossClient.putObject(bucketName, objectName, file.getInputStream());
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        String path = "https://" + bucketName + "." + endpoint + "/" + objectName;
        return  new JsonResult(200,"aaaa",path);
    }
}
