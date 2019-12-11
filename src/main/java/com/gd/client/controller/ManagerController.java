package com.gd.client.controller;

import com.gd.client.bean.DaTableAjax;
import com.gd.client.bean.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ManagerController {

    @RequestMapping("queryGoodInfo")
    public DaTableAjax queryGoodInfo(HttpServletRequest request){

        ResponseData responseData = new ResponseData();

        responseData.setCode("1");
        responseData.setMessage("查询成功");
        responseData.setData("{");
        return null;
    }
}
