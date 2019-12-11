package com.gd.client.bean;

import lombok.Data;

@Data
public class Good {
    private Integer id;
    private String goodName;
    private String goodPrice;
    private String goodUnit;
    private Integer goodPartFlag;
    private String imgUrl;
    private Integer goodSaleTimes;
}
