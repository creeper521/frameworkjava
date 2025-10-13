package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DictionaryTypeVO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;

public interface ISysDictionaryService {
    Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);

    BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO
                                                  dictionaryTypeListReqDTO);

    Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);
}
