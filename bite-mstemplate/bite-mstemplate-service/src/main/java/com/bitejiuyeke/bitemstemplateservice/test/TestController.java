
package com.bitejiuyeke.bitemstemplateservice.test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {
    @GetMapping("/info")
    public void info(){
        log.info("测试");
    }

}
