//package com.bitejiuyeke.bitemstemplateservice.test;
//
//import com.bitejiuyeke.bitecommondomain.domain.R;
//import com.bitejiuyeke.bitecommonredis.service.RedisService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Slf4j
//@RequestMapping("/test/redis")
//public class TestRedisController {
//
//    @Autowired
//    private RedisService redisService;
//
//    @PostMapping("/add")
//    public void add(){
//        redisService.setCacheObject("test", "test~");
//    }
//}
