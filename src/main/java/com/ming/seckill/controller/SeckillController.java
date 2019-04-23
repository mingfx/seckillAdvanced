package com.ming.seckill.controller;

import com.ming.seckill.domain.OrderInfo;
import com.ming.seckill.domain.SeckillOrder;
import com.ming.seckill.domain.SeckillUser;
import com.ming.seckill.result.CodeMsg;
import com.ming.seckill.service.OrderService;
import com.ming.seckill.service.SeckillGoodsService;
import com.ming.seckill.service.SeckillService;
import com.ming.seckill.vo.SeckillGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping("/do_seckill")
    public String doSeckill(Model model,
                            SeckillUser seckillUser,
                            @RequestParam("goodsId") long goodsId){
        if (seckillUser==null){
            return "login";
        }
        model.addAttribute("user",seckillUser);
        //判断库存
        SeckillGoodsVo goodsVo = seckillGoodsService.getSeckillGoodsVoById(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock<0){
            //库存不足，返回失败信息
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return "seckill_fail";
        }
        //判断是否已经秒杀过了
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(),goodsId);
        if (seckillOrder!=null){
            //重复秒杀，返回失败信息
            model.addAttribute("errmsg",CodeMsg.SECKILL_REPEAT.getMsg());
            return "seckill_fail";
        }
        //秒杀,成功返回订单详情
        OrderInfo orderInfo = seckillService.seckill(seckillUser,goodsVo);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        return "order_detail";
    }
}
