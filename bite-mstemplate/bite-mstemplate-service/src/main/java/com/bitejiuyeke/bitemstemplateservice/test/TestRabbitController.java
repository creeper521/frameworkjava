//package com.bitejiuyeke.bitemstemplateservice.test;
//
//import com.bitejiuyeke.bitecommondomain.domain.R;
//import com.bitejiuyeke.bitemstemplateservice.domain.MessageDTO;
//import com.bitejiuyeke.bitemstemplateservice.rabbit.Producer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Slf4j
//@RequestMapping("/test/rabbit")
//public class TestRabbitController {
//
//    @Autowired
//    private Producer producer;
//
//    @RequestMapping("/send")
//    public R<Void> send(){
//        MessageDTO messageDTO = new MessageDTO();
//        messageDTO.setType("哈吉米");
//        messageDTO.setDesc("顶冬季");
//        producer.produceMsg(messageDTO);
//        return R.ok();
//    }
//
//}
