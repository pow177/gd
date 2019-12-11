package com.gd.client.service;

import com.gd.client.bean.DaTableAjax;
import com.gd.client.bean.DataTableOfGoodPack;
import com.gd.client.bean.Good;
import com.gd.client.dao.GoodsMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class GoodServiceImpl {
    @Autowired
    GoodsMapper goodsMapper;
    public DaTableAjax checkProductDataListBySearch(int page, int size, int draw, String search){
        page = page/ size + 1;
        PageHelper.startPage(page, size);
        List<Good> list = goodsMapper.getGoodInfoBySearch(search);
        List<DataTableOfGoodPack> proPacks = new ArrayList<DataTableOfGoodPack>();

        for(Good good:list){
            proPacks.add(new DataTableOfGoodPack(good.getGoodName(),good.getGoodPrice(),good.getGoodUnit(),good.getGoodPartFlag(),good.getImgUrl(),good.getGoodSaleTimes());
            //(String topPic,String proName,String proId,float proPrice){
        }
        DaTableAjax<DataTableOfGoodPack> daTableAjax = new DaTableAjax<DataTableOfGoodPack>();
        daTableAjax.setData(proPacks);
        daTableAjax.setDraw(draw);
        long s = (long) goodsMapper.getGoodsCounts();
        daTableAjax.setRecordsFiltered(s); //加多一个mapper，查所有记录的count
        daTableAjax.setRecordsTotal(s);
        return daTableAjax;
    }

    public String[] insertGoodInfo(String name,String price,String partFlag,String unit, MultipartFile file){
        Assert.notNull(name);
        Assert.notNull(price);
        Assert.notNull(partFlag);
        Assert.notNull(unit);

        String imgUrl = "";
        goodsMapper.insertGoodInfo(name,unit,price,partFlag,imgUrl);

    }
}
