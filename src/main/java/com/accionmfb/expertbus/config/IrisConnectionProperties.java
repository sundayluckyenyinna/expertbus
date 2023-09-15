package com.accionmfb.expertbus.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "iris.server")
public class IrisConnectionProperties {

    private String host = "localhost";

    private Integer port = 8080;

    private String protocol = "http";

    @Value("${request.logging:default}")
    private String requestLogging;

    @Value("${response.logging:default}")
    private String responseLogging;

    @Value("${startup.connection.logging:default}")
    private String startupConnectionLogging;

}
