package com.accionmfb.expertbus.payload.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IrisHttpHeader
{
    private String transactionStatus;
    private IrisHttpHeaderAudit audit;
    private String id;
    private String status;
}
