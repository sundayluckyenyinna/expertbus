package com.accionmfb.expertbus;

import com.accionmfb.expertbus.config.IrisConnectionProperties;
import com.accionmfb.expertbus.data.IrisConnection;
import com.accionmfb.expertbus.manager.IrisServerManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Socket;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = IrisConnectionProperties.class)
public class ExpertbusConfig {
    private final IrisConnectionProperties irisConnectionProperties;

    @Bean
    public CommandLineRunner pinIrisServerOnStartup(){
        String irisHost = irisConnectionProperties.getHost();
        Integer irisPort = irisConnectionProperties.getPort();

        log.info("Attempting Iris Server connection with Host: {}, and port: {}", irisHost, irisPort);
        return (args -> {
            IrisConnection irisConnection = IrisServerManager.getIrisConnection(irisHost, irisPort);
            Socket socket = irisConnection.getConnectionSocket();
            if(Optional.ofNullable(socket).isPresent()){
                log.info("Iris Server ({}) found for connection. Attempting connection on port: {}", irisHost, irisPort);
                if(socket.isConnected()){
                    log.info("Iris Server established connection with following details:");
                    String logging = irisConnectionProperties.getStartupConnectionLogging();
                    if(logging.equalsIgnoreCase("default")) {
                        logIrisServerStartupConnectionProperties(irisHost, irisPort, true);
                    }
                }else{
                    log.error("Iris Server could not establish connection with following details:");
                    logIrisServerStartupConnectionProperties(irisHost, irisPort, false);
                }
            }else{
                log.error("Could not find Iris Server for connection.");
                log.error("Iris Server could not establish connection with following details:");
                logIrisServerStartupConnectionProperties(irisHost, irisPort, false);
            }
        });
    }

    private void logIrisServerStartupConnectionProperties(String host, Integer port, boolean isConnected){
        log.info("============================================= IRIS SERVER CONNECTION PROPERTIES =============================================");
        log.info("Host Address: {}", host);
        log.info("Server Port: {}", port);
        if(isConnected) {
            log.info("Status: {}", "UP | CONNECTED");
        }else{
            log.error("Status: {}", "DOWN | DISCONNECTED");
        }
        log.info("=============================================================================================================================");
    }
}
