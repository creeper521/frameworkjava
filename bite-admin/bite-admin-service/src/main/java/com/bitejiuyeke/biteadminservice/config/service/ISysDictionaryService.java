package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeWriteReqDTO;

public interface ISysDictionaryService {
    Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);
}
