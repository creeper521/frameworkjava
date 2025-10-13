package com.bitejiuyeke.biteadminapi.config.domain.dto;

import com.bitejiuyeke.bitecommondomain.domain.dto.BasePageReqDTO;
import lombok.Data;

@Data
public class DictionaryTypeListReqDTO extends BasePageReqDTO {
    private String value;
    private String typeKey;

}
