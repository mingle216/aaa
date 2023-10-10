package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.common.annotations.Login.MustLogin;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能描述：Mybatis 缓存操作类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title DevController
 * @Author: 01116267
 * @Date: 2021/11/29
 */
@Controller
@RequestMapping("/dev/mybatis")
public class DevController extends AbstractController{
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/refreshCache")
    @ResponseBody
    @MustLogin(restrictUser = "ampadmin")
    public Map<String, Object> refreshCache(@RequestParam("cacheKey") String cacheKey){
        if("all".equals(cacheKey)){
            Set<String> cacheKeys = redisUtil.scan("com.wisedu.minos.casp.portal.dao.mapper");
            cacheKeys.forEach(item -> {
                redisUtil.del(item);
            });
            return success();
        }else if(cacheKey.startsWith("com.wisedu.minos.casp.portal.dao.mapper")){
            redisUtil.del(cacheKey);
            return success();
        }else{
            return error("缓存key值不合法!");
        }
    }

    @RequestMapping("/getAllCache")
    @ResponseBody
    @MustLogin(restrictUser = "ampadmin")
    public Map<String, Object> getAllCache(){
        Map<String, Integer> result = new HashMap<>();
        Set<String> cacheKeys = redisUtil.scan("com.wisedu.minos.casp.portal.dao.mapper");
        if( !CollectionUtils.isEmpty(cacheKeys) ){
            result = cacheKeys.stream().collect(Collectors.toMap(String::toString, item -> redisUtil.hashSize(item)));
        }
        return success(result);
    }

}
