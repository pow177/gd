package com.gd.client.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 黄冠莳 on 2018/4/22.
 */
public class FileCheck {

    public static int checkPic(MultipartFile[] imgs) {
        for(MultipartFile img:imgs) {
            String cStr = img.getOriginalFilename();
            String regEx = "jpg$|jpeg$|png$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(cStr);
            boolean result = m.find();

            //后台验证文件是否为空
            if (img == null) {
                return 0;
            }
            if (!result) { //格式错误
                return 0;
            }
        }
        return 1;
    }
    public static int checkPic(MultipartFile img){
        String cStr = img.getOriginalFilename();
        String regEx = "jpg$|jpeg$|png$";
        Pattern p= Pattern.compile(regEx);
        Matcher m=p.matcher(cStr);
        boolean result=m.find();
        System.out.println("result: "+result);
        //后台验证文件是否为空
        if(img==null){
            return 0;
        }
        if(!result){ //格式错误
            return 0;
        }
        return 1;
        //存图片的路径
//       String imgSaveUrl = "D:\\idea_WordSpace\\maven2-ssm\\target\\pow-1.0-SNAPSHOT\\resources\\userImgTem\\";// "D:\\idea_WordSpace\\maven2-ssm\\src\\main\\webapp\\resources\\userImgTem\\";
//        String imgSaveUrl  = request.getRealPath("/userImgTem");        //这样获取会少一个resources目录，不知道为什么
//
//        System.out.println("地址:  "+imgSaveUrl);
//        String orgFileName = img.getOriginalFilename();
//        //原始图片名称
//        //新图片名称
//        String newFileName = UUID.randomUUID()+orgFileName.substring(orgFileName.lastIndexOf(".")); //uuid随机数+后缀名
//
//        File newFile = new File(imgSaveUrl+"/"+newFileName);
//        //内存的数据=>磁盘中
//        img.transferTo(newFile);
    }
    public static int checkFile(MultipartFile file){
        if(file==null){
            return 0;
        }else{
            return 1;
        }
    }
    public static int checkFile(MultipartFile[] files){
        for(MultipartFile file:files) {
            if (file == null) {
                return 0;
            }
        }
        return 1;
    }

    public static String getFileOrgName(MultipartFile file){
        return file.getOriginalFilename().split("\\.")[0];
    }
    public static String getFileOrgName(MultipartFile[] files){
        String resultStr = "";
        for(int i=files.length-1;i>=0;i--){
            resultStr = files[i].getOriginalFilename().split("\\.")[0]+","+resultStr;
        }
        return resultStr;
    }
}
