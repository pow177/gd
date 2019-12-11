package com.gd.client.service;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 黄冠莳 on 2018/4/25.
 */
@Service
public class QnServiceImpl {
    private static final Log log = LogFactory.getLog(QnServiceImpl.class);
    Configuration cfg = new Configuration(Zone.zone0());
    public static String privateUptoken = null;
    public static String publicUptoken = null;
    public UploadManager uploadManager = new UploadManager(cfg);

    @Value("${qn_ak}")
    private String ak;

    @Value("${qn_sk}")
    private String sk;

    @Value("${qn_publicBucketname}")
    private String publicBucketname;

    @Value("${qn_privateBucketname}")
    private String privateBucketname;

    @Value("${qn_public_url}")
    private String qn_public_url;

    @Value("${qn_private_url}")
    private String qn_private_url;

    public String getQn_public_url() {
        return qn_public_url;
    }

    public String getQn_private_url() {
        return qn_private_url;
    }

    public String getAk() {
        return ak;
    }

    public String getSk() {
        return sk;
    }

    public String getPublicBucketname() {
        return publicBucketname;
    }

    public String getPrivateBucketname() {
        return privateBucketname;
    }

    /**
     * 获取token
     * @return
     */
    public String getUptoken(String bucket){
        return Auth.create(ak, sk).uploadToken(bucket);
    }

    /**
     * 生成uptoken
     */
    //@PostConstruct
    public void genUptoken(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                privateUptoken = getUptoken(publicBucketname);
                publicUptoken = getUptoken(privateBucketname);
            }
        }, 0, 3500, TimeUnit.SECONDS);
    }

    private String privateDownloadUrl(String baseUrl, long expires){
        return Auth.create(ak, sk).privateDownloadUrl(baseUrl, expires);
    }

    private String privateDownloadUrl(String baseUrl){
        return Auth.create(ak, sk).privateDownloadUrl(baseUrl);
    }

    public String privateDownloadUrl(String baseUrl, boolean isPrivate){
        return isPrivate ? privateDownloadUrl(baseUrl) : baseUrl;
    }

    public String privateDownloadUrl(String baseUrl, long expires, boolean isPrivate){
        return isPrivate ? privateDownloadUrl(baseUrl, expires) : baseUrl;
    }


    public void uploadFile(String path, String file, boolean isPrivate){
        try {
            uploadManager.put(file, path, isPrivate ?  Auth.create(ak, sk).uploadToken(privateBucketname) : Auth.create(ak, sk).uploadToken(publicBucketname));
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }


    public boolean uploadFile(String key, byte[] data, boolean isPrivate){
        try {
            Response response = uploadManager.put(data, key, isPrivate ?   Auth.create(ak, sk).uploadToken(privateBucketname) : Auth.create(ak, sk).uploadToken(publicBucketname) );
            log.debug(response);
            return response!=null && response.statusCode==200 && StringUtils.isEmpty(response.error)? true : false;
        } catch (QiniuException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String getUrl(boolean isPrivate){
        return isPrivate? qn_private_url : qn_public_url;
    }

    public String fileUploadAndGetUrl(MultipartFile file, int type) throws IOException {
        String result = "";
        String saveFileType = "pro_default";
        if(type==1){
            saveFileType="pro_img";
        }else if(type==2){
            saveFileType="pro_file";
        }
        String newFileName = ""+ UUID.randomUUID();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String key = saveFileType+"/"+ newFileName + suffix;
        System.out.println("keyName-------->"+key);
        String path="";
        System.out.println("qnService---->"+this);
        if(!this.uploadFile(key, file.getBytes(), false)){
            result = "上传失败，请重新上传";
            System.out.println(result);
            throw new IOException("上传失败，请重新上传");
        }
        path = this.getUrl(false) +"/"+key;   //访问路径
//        String fileUrl = saveUrl + "/" + newFileName;
//        File newFile = new File(fileUrl);
        //内存的数据=>磁盘中
//        file.transferTo(newFile);
        return path;
    }
    //上传文件并且返回文件存储的地址
    public String fileUploadAndGetUrl(MultipartFile[] files, int type) throws IOException {
        String saveFileType = "pro_default";
        String result = "";
        if(type==1){
            saveFileType="pro_img";
        }else if(type==2){
            saveFileType="pro_file";
        }

        String fileUrls = "";   //把多个地址串成一个字符串
        for(MultipartFile file:files) {
            String newFileName = ""+ UUID.randomUUID();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String key = saveFileType+"/"+ newFileName + suffix;
            String path="";
            if(!this.uploadFile(key, file.getBytes(), false)){
                result = "上传失败，请重新上传";
                System.out.println(result);
                throw new IOException("上传失败，请重新上传");
            }
            path = this.getUrl(false) +"/"+key;   //访问路径
//           fileUrl = (saveUrl + "/" + newFileName);
//           File newFile = new File(fileUrl);
            fileUrls = path+","+fileUrls;
//           //内存的数据=>磁盘中
//           file.transferTo(newFile);
        }
        return fileUrls;       //url1,url2,url3...
    }
    /**
     * 上传文件并返回url(不包括空间名)
     * @param file 上传的文件数据
     * @param type 上传的文件类型
     * @return
     * @throws IOException
     */
    public String fileUploadAndGetUrlNoBucket(MultipartFile file, int type) throws IOException {
        String result = "";
        String saveFileType = "pro_default";
        if(type == 1) {
            // type == 1时表示上传的是图片
            saveFileType="pro_img";
        } else if(type == 2) {
            // type == 2时表示上传的是文档
            saveFileType="pro_file";
        } else if (type == 3) {
            // type == 3时表示上传的是视频
            saveFileType = "pro_video";
        }
        String newFileName = ""+ UUID.randomUUID();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String key = saveFileType+"/"+ newFileName + suffix;
        if(!this.uploadFile(key, file.getBytes(), false)){
            result = "上传失败，请重新上传";
            System.out.println(result);
            throw new IOException("上传失败，请重新上传");
        }
        // 修改返回值，不包括空间名
        return key;
    }

    /**
     * 覆盖上传的文件，因为缓存的原因，可能这边更新后在七牛那边查询的结果还是旧文件，需要加上?v=20160331参数从源文件处获取
     * @param key 原文件名
     * @param data  上传的文件数据
     * @param isPrivate 上传到私有空间还是公有空间
     * @return
     */
    public boolean updateFile(String key, byte[] data, boolean isPrivate){
        // 上传策略
        StringMap policy = new StringMap();
        String token;
        if (isPrivate) {
            // 该上传策略为指定只能上传特定key的文件，该策略如果存在同名的文件会被覆盖
            policy.put("scope", privateBucketname + ":" + key);
            token = Auth.create(ak, sk).uploadToken(privateBucketname,key ,3600, policy);
        } else {
            policy.put("scope", publicBucketname + ":" + key);
            token = Auth.create(ak, sk).uploadToken(publicBucketname, key,3600, policy);
        }
        try {
            Response response = uploadManager.put(data, key, token);
            return response!=null && response.statusCode==200 && StringUtils.isEmpty(response.error)? true : false;
        } catch (QiniuException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除七牛云的文件,删除成功后七牛云管理界面是搜索不到删除的资源了，不过因为CDN上还存在缓存，所以还是能访问到，需要加上?v=20160331参数从源文件处获取
     * @param bucketType 空间名，如果是0表示要删的是公有空间的资源，1则表示要删私有空间的资源
     * @param fileName 资源名
     * @return 删除失败返回0，删除成功返回1
     */
    public Integer deleteFile(Integer bucketType, String fileName) {
        // 资源管理对象
        BucketManager bucketManager = new BucketManager(Auth.create(ak,sk), cfg);
        // 七牛云返回结果
        Response response = null;
        try {
            if (bucketType == 0) {
                response = bucketManager.delete(publicBucketname, fileName);
            } else if (bucketType == 1) {
                response = bucketManager.delete(privateBucketname, fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  0;
        }
        // 七牛云返回的不是200,则表示删除失败
        if (response.statusCode != 200) {
            return 0;
        }
        return 1;
    }

    /**
     * 更新七牛云文件
     * @param file
     * @param key 原文件名（根据原文件名进行覆盖）
     * @param isPrivate true表示更新的是私有空间的资源，false表示更新的是公有空间的资源
     * @return
     * @throws IOException
     */
    public void updateFile(MultipartFile file, String key, Boolean isPrivate) throws IOException {
        if(!this.updateFile(key, file.getBytes(), false)){
            throw new IOException("更新失败，请重新上传");
        }
    }

}
