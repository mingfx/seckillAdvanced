package com.ming.seckill.service;

import com.ming.seckill.domain.OrderInfo;
import com.ming.seckill.domain.SeckillUser;
import com.ming.seckill.vo.SeckillGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo seckill(SeckillUser seckillUser, SeckillGoodsVo goodsVo){
        //减库存
        seckillGoodsService.reduceStock(goodsVo);
        //写订单(两个表）
        return orderService.createOrder(seckillUser,goodsVo);
    }
}
