package com.ming.seckill.controller;

import com.ming.seckill.domain.SeckillUser;
import com.ming.seckill.redis.RedisService;
import com.ming.seckill.redis.SeckillUserKey;
import com.ming.seckill.result.Result;
import com.ming.seckill.service.SeckillGoodsService;
import com.ming.seckill.service.SeckillUserService;
import com.ming.seckill.vo.LoginVo;
import com.ming.seckill.vo.SeckillGoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @RequestMapping("/to_list")
    public String toList(Model model,HttpServletResponse response,
                          //@CookieValue(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
                          //@RequestParam(value = SeckillUserService.COOKIE_NAME_TOKEN,required = false) String paramToken,
                         SeckillUser seckillUser){
//        if (StringUtils.isEmpty(cookieToken)&& StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        SeckillUser seckillUser = seckillUserService.getByToken(response,token);
        model.addAttribute("user",seckillUser);
        //查询商品列表
        List<SeckillGoodsVo> list = seckillGoodsService.getListSeckillGoodsVo();
        model.addAttribute("goodsList",list);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model, HttpServletResponse response,
                           @PathVariable("goodsId") long goodsId,
                           SeckillUser seckillUser){
        SeckillGoodsVo seckillGoodsVo = seckillGoodsService.getSeckillGoodsVoById(goodsId);
        long startTime = seckillGoodsVo.getStartDate().getTime();
        long endTime = seckillGoodsVo.getEndDate().getTime();
        long now = new Date().getTime();
        int remainSeconds = 0;
        int seckillStatus = 0;//0未开始，1正在进行，2已结束
        if (now<startTime){
            //秒杀未开始
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now)/1000);
        }else if (now > endTime){
            //秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            //秒杀正在进行
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("goods",seckillGoodsVo);
        model.addAttribute("user",seckillUser);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("seckillStatus", seckillStatus);
        return "goods_detail";
    }
}
