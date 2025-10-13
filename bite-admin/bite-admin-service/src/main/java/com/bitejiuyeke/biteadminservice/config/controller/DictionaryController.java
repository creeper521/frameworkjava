package com.bitejiuyeke.biteadminservice.config.controller;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryDataDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DictionaryTypeVO;
import com.bitejiuyeke.biteadminapi.config.feign.DictionaryFeignClient;
import com.bitejiuyeke.biteadminservice.config.service.ISysDictionaryService;
import com.bitejiuyeke.bitecommondomain.domain.R;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DictionaryController implements DictionaryFeignClient {

    @Resource
    private ISysDictionaryService iSysDictionaryService;

    @PostMapping("/dictionary_type/add")
    public R<Long> addType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return R.ok(iSysDictionaryService.addType(dictionaryTypeWriteReqDTO));
    }

    @PostMapping("/dictionary_data/list")
    public R<BasePageVO<DictionaryTypeVO>> listType(@RequestBody @Validated DictionaryTypeListReqDTO dictionaryTypeListReqDTO) {
        return R.ok(iSysDictionaryService.listType(dictionaryTypeListReqDTO));
    }

    @PostMapping("/dictionary_type/edit")
    public R<Long> editType(@RequestBody @Validated DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO) {
        return R.ok(iSysDictionaryService.editType(dictionaryTypeWriteReqDTO));
    }

    @Override
    public List<DictionaryDataDTO> selectDicDataByType(String typeKey) {
        return iSysDictionaryService.selectDicDataByType(typeKey);
    }

    @Override
    public Map<String, List<DictionaryDataDTO>> selectDicDataByTypes(List<String> typeKeys) {
        return iSysDictionaryService.selectDicDataByTypes(typeKeys);
    }

    @Override
    public DictionaryDataDTO getDicDataByKey(String dataKey) {
        return iSysDictionaryService.selectDictDataByDataKey(dataKey);
    }

    @Override
    public List<DictionaryDataDTO> getDictDataByDataKeys(List<String> dataKeys) {
        return iSysDictionaryService.getDicDataByKeys(dataKeys);
    }
}
