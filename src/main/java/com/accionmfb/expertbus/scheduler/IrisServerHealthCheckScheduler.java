package com.accionmfb.expertbus.scheduler;

import com.accionmfb.expertbus.config.IrisConnectionProperties;
import com.accionmfb.expertbus.data.IrisConnection;
import com.accionmfb.expertbus.manager.IrisServerManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;


@Slf4j
@Configuration
@EnableScheduling
@EnableConfigurationProperties(value = IrisConnectionProperties.class)
@RequiredArgsConstructor
public class IrisServerHealthCheckScheduler {

    private final IrisConnectionProperties irisConnectionProperties;

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 1000)
    public void pingIrisConnectionServer() throws IOException {
        String irisHost = irisConnectionProperties.getHost();
        Integer irisPort = irisConnectionProperties.getPort();
        if(Optional.ofNullable(irisHost).isPresent() && Optional.ofNullable(irisPort).isPresent()) {
            IrisConnection irisConnection = IrisServerManager.getIrisConnection(irisHost, irisPort);
            Socket rawIrisSocket = irisConnection.getConnectionSocket();
            if(Optional.ofNullable(rawIrisSocket).isPresent()){
                if(!rawIrisSocket.isConnected()){
                    log.info("Iris Server located with status: {}", "DOWN | DISCONNECTED");
                }
                rawIrisSocket.close();
            }else{
                log.warn("Could not locate Iris Connection server on port: {}. It is either the server is down or the port is blocked of connections!", irisPort);
            }
        }
    }

}
