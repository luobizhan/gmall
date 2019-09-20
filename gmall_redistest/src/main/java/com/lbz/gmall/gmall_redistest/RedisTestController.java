package com.lbz.gmall.gmall_redistest;

import com.lbz.gmall.util.RedisUtil;
import org.jsoup.helper.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lbz
 * @create 2019-08-25 17:16
 */
@Controller
public class RedisTestController {

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private RedissonClient redissonClient;

    @ResponseBody
    @RequestMapping("/")
    public String testRedis(){

        RLock lock = redissonClient.getLock("lock");

        try {
            lock.lock();
            String v = redisUtil.get("k").toString();
            if(StringUtil.isBlank(v)){
                v = "1" ;
            }
            System.out.println(v);
            redisUtil.set("k",(Integer.parseInt(v)+1));
            return "success" ;
        } finally {

            lock.unlock();
        }
    }

}
