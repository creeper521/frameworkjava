//
//package com.bitejiuyeke.bitemstemplateservice.test;
//
//import com.bitejiuyeke.bitecommondomain.domain.R;
//
//import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestController
//@RestControllerAdvice
//@Slf4j
//@RequestMapping("/test")
//public class TestController {
//    @GetMapping("/info")
//    public void info(){
//        log.info("测试");
//    }
//
//    @GetMapping("/result")
//    public R<Void> result(int id) {
//        if (id < 0) {
//            return R.fail();
//        }
//        return R.ok();
//    }


//    @GetMapping("/exception")
//    public R<Void> exception(int id) {
//        if (id < 0) {
//            throw new ServiceException(ResultCode.INVALID_CODE);
//        }
//        if (id == 1) {
//            throw new ServiceException("id不能为1");
//        }
//        if (id == 1000) {
//            throw new ServiceException("id不能为1000", ResultCode..getCode());
//        }
//        return R.ok();
//    }




//}
