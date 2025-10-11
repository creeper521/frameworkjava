package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class GeoResultDTO extends QQMapBaseResponseDTO {
    /**
     * 结果信息
     */
    private AddrResultDTO result;
}