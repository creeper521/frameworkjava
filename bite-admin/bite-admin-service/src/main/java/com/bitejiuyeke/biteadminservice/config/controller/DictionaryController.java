package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class DictionaryController {

    @Resource
    private ISysDictionaryService iSysDictionaryService;

    @PostMapping("/dictionary_type/add")
    public R<Long> addType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {

        return R.ok(iSysDictionaryService.addType(dictionaryTypeWriteReqDTO));
    }
}
