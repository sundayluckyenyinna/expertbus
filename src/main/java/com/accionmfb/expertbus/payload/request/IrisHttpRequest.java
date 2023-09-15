package com.accionmfb.expertbus.payload.request;

import com.accionmfb.expertbus.payload.response.IrisHttpHeader;
import lombok.Data;

@Data
public class IrisHttpRequest<T> {
    private IrisHttpHeader header;
    private T body;
}
