package com.bitejiuyeke.biteadminapi.config.feign;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "dictionaryFeignClient", value = "bite-admin")
public interface DictionaryFeignClient {
    @GetMapping("/dictionary_data/type")
    List<DictionaryDataDTO> selectDicDataByType(@RequestParam String typeKey);
}
