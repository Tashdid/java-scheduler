package com.tigerit.smartbill.scheduler.dto;

import lombok.Data;

@Data
public class RequestMetadata {
    private String hostName;
    private String remoteAddress;
    private String clientIPAddress;
}
