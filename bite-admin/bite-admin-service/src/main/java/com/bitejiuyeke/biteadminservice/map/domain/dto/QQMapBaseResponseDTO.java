package com.bitejiuyeke.biteadminservice.map.domain.dto;

import lombok.Data;

@Data
public class QQMapBaseResponseDTO {
    /**
     * 响应码 0表⽰成功
     */
    private int status;
    /**
     * 响应消息
     */
    private String message ;
    /**
     * 请求id
     */
    private String request_id;
}
