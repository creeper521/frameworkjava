package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "dictionaryFeignClient", value = "bite-admin")
public interface DictionaryFeignClient {
    @GetMapping("/dictionary_data/type")
    List<DictionaryDataDTO> selectDicDataByType(@RequestParam String typeKey);

    @PostMapping("/dictionary_data/types")
    Map<String, List<DictionaryDataDTO>> selectDicDataByTypes(@RequestBody List<String> typeKeys);

    @GetMapping("/dictionary_data/key")
    DictionaryDataDTO getDicDataByKey(@RequestParam String dataKey);

    @GetMapping("/dictionary_data/keys")
    List<DictionaryDataDTO> getDictDataByDataKeys(@RequestParam List<String> dataKeys);
}
