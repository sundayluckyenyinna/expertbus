package com.accionmfb.expertbus.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IrisHttpHeaderAudit
{
    @JsonProperty(value = "T24_time")
    private String t24Time;

    @JsonProperty(value = "responseParse_time")
    private String responseParseTime;

    @JsonProperty(value = "requestParse_time")
    private String requestParseTime;

    private String versionNumber;
}
