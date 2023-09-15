package com.accionmfb.expertbus.payload.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IrisHttpResponse<T>
{
    private IrisHttpHeader header;
    private T body;

    public boolean isSuccess(){
        return this.getHeader().getStatus().equalsIgnoreCase("success");
    }
}
