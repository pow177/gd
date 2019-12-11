package com.gd.client.bean;


import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 黄冠莳 on 2018/4/24.
 */
@Data
public class DataTableOfGoodPack {
    private Integer id;
    private String goodName;
    private String goodPrice;
    private String goodUnit;
    private String goodPartFlag;
    private String imgUrl;
    private String goodSaleTimes;

    public DataTableOfGoodPack(String goodName, String goodPrice, String goodUnit, int goodPartFlag,String imgUrl,int goodSaleTimes){
        this.goodName = "";
        this.goodPrice = "";
        this.goodUnit = "";
        this.goodPartFlag = "";
        this.imgUrl = "";
        this.goodSaleTimes = "";
    }

}
