package com.zxst.shoop.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zxst.shoop.entity.User;
import com.zxst.shoop.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/uploadpic")
public class FileController extends BaseController {

    @Resource
    private UserService userService;

    @Value("${alinfo.oss-endpoint}")
    private String endpoint;
    @Value("${alinfo.oss-accesskeyid}")
    private String accessKeyId;
    @Value("${alinfo.oss-accesskeysecret}")
    private String accessKeySecret;
    @Value("${alinfo.oss-bucketname}")
    private String bucketName;

    @RequestMapping("/savePic")
    public String savePic(@RequestParam("file") MultipartFile file, HttpSession session) {
        System.out.println(endpoint+"\t"+accessKeyId+"\t"+accessKeySecret+"\t"+bucketName+"\t"+file.getOriginalFilename());
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String filename = file.getOriginalFilename(); // 4.5.6.7.82.jpg
        String uuidStr = UUID.randomUUID().toString().replace("-",""); // uuid字符串
        System.out.println(uuidStr);
        filename = filename.substring(filename.lastIndexOf("."));
        String newFileName = uuidStr + filename;
        String userName = getUserName(session);
        String objectName = userName+"/"+newFileName;
        // 创建OSS对象
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //上传
            ossClient.putObject(bucketName, objectName, file.getInputStream());
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        //构建path  https://zxst-shoop-new.oss-cn-qingdao.aliyuncs.com/admin.jpg
        String path = "https://zxst-shoop-new.oss-cn-qingdao.aliyuncs.com/" + objectName;
        User user = new User();
        user.setAvatar(path);
        user.setModifiedTime(new java.util.Date());
        user.setModifiedUser(getUserName(session));
        user.setUid(getUserId(session));
        boolean b = userService.eeditUserInfo(user);
        if (b){
            return "redirect:/web/upload.html";
        }else{
            return "redirect:/web/500.html";
        }
    }



}
