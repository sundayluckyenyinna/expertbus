package com.accionmfb.expertbus.manager;

import com.accionmfb.expertbus.data.IrisConnection;

import java.net.Socket;

public class IrisServerManager {

    private static Socket getIrisServerSocket(String irisHost, Integer irisPort){
        Socket socket = null;
        try{
            socket = new Socket(irisHost, irisPort);
        }catch (Exception ignored){}
        return socket;
    }

    public static IrisConnection getIrisConnection(String host, Integer port){
        return IrisConnection.builder()
                .host(host)
                .port(port)
                .connectionSocket(getIrisServerSocket(host, port))
                .build();
    }
}
