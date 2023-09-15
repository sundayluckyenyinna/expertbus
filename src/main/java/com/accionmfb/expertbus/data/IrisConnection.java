package com.accionmfb.expertbus.data;

import lombok.Builder;
import lombok.Data;

import java.net.Socket;

@Data
@Builder
public class IrisConnection {
    private String host;
    private Integer port;
    private Socket connectionSocket;
}
