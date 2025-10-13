package com.bitejiuyeke.biteadminservice.config.service;

import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryDataDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeListReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.dto.DictionaryTypeWriteReqDTO;
import com.bitejiuyeke.biteadminapi.config.domain.vo.DictionaryTypeVO;
import com.bitejiuyeke.bitecommondomain.domain.vo.BasePageVO;

import java.util.List;
import java.util.Map;

public interface ISysDictionaryService {
    Long addType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);

    BasePageVO<DictionaryTypeVO> listType(DictionaryTypeListReqDTO
                                                  dictionaryTypeListReqDTO);

    Long editType(DictionaryTypeWriteReqDTO dictionaryTypeWriteReqDTO);

    List<DictionaryDataDTO> selectDicDataByType(String dicTypeCode);

    Map<String, List<DictionaryDataDTO>> selectDicDataByTypes(List<String> typeKeys);

    DictionaryDataDTO selectDictDataByDataKey(String dataKey);

    List<DictionaryDataDTO> getDicDataByDataKeys(List<String> dataKeys);

}
