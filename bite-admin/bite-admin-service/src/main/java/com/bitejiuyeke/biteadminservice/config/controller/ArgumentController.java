package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentAddReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentEditReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.ArgumentListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.ArgumentVO;
import com.bitejiuyeke.biteadminapi.config.feign.ArgumentFeignClient;
import com.bitejiuyeke.biteadminservice.config.service.ISysArgumentService;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/argument")
public class ArgumentController implements ArgumentFeignClient {
    @Resource
    private ISysArgumentService iSysArgumentService;

    @PostMapping("/add")
    public R<Long> add(@RequestBody ArgumentAddReqDTO argumentAddReqDTO) {
        return R.ok(iSysArgumentService.add(argumentAddReqDTO));
    }

    @GetMapping("/list")
    public R<BasePageVO<ArgumentVO>> list(@Validated ArgumentListReqDTO argumentListReqDTO) {
        return R.ok(iSysArgumentService.list(argumentListReqDTO));
    }

    @PostMapping("/edit")
    public R<Long> edit(@RequestBody ArgumentEditReqDTO argumentEditReqDTO) {
        return R.ok(iSysArgumentService.edit(argumentEditReqDTO));
    }

    @Override
    public ArgumentDTO getByConfigKey(String configKey) {
        return iSysArgumentService.getByConfigKey(configKey);
    }

    @Override
    public List<ArgumentDTO> getByConfigKeys(List<String> configKeys) {
        return iSysArgumentService.getByConfigKeys(configKeys);
    }
}
