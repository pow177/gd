package com.gd.client.dao;

import com.gd.client.bean.Good;

import java.util.List;

public interface GoodsMapper {
    List<Good> getGoodInfoBySearch(String search);
    void insertGoodInfo(String goodName,String goodUnit,String goodPrice,String goodPartFlag,String imgUrl);
    void deleteGoodById(String id);
    int getGoodsCounts();
}
