package com.ming.seckill.service;

import com.ming.seckill.dao.OrderDAO;
import com.ming.seckill.domain.OrderInfo;
import com.ming.seckill.domain.SeckillOrder;
import com.ming.seckill.domain.SeckillUser;
import com.ming.seckill.vo.SeckillGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    OrderDAO orderDAO;

    public SeckillOrder getSeckillOrderByUserIdGoodsId(long userId,long goodsId){
        return orderDAO.getSeckillOrderByUserIdGoodsId(userId,goodsId);
    }

    @Transactional
    public OrderInfo createOrder(SeckillUser seckillUser, SeckillGoodsVo goodsVo){
        //orderInfo
        OrderInfo newOrder = new OrderInfo();
        newOrder.setCreateDate(new Date());
        newOrder.setGoodsCount(1);
        newOrder.setGoodsName(goodsVo.getGoodsName());
        newOrder.setGoodsId(goodsVo.getId());
        newOrder.setGoodsPrice(goodsVo.getSeckillPrice());
        newOrder.setUserId(seckillUser.getId());
        newOrder.setDeliveryAddrId(0L);
        newOrder.setOrderChannel(1);
        newOrder.setPayDate(new Date());
        //TODO status用枚举类型
        newOrder.setStatus(0);
        long res = orderDAO.insertOrderInfo(newOrder);
        //seckillOrder
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(newOrder.getId());
        seckillOrder.setUserId(seckillUser.getId());
        orderDAO.insertSeckillOrder(seckillOrder);
        return newOrder;
    }
}
