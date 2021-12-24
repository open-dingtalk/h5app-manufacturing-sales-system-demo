package com.dingtalk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xueyu
 */
@Slf4j
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    /**
     * port
     */
    private static int port;
    /**
     * contentPath
     */
    private static String contentPath;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("application run success!  >>>>>>> ");
        log.info("- Local:   http://localhost:{}{}", port, contentPath);
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            log.info("- Network: http://{}:{}{}", ip, port, contentPath);
        } catch (UnknownHostException e) {
            log.error("get local host fail: {}", e.getMessage());
        }
    }



    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Value("${server.port}")
    public void setPort(int port) {
        Application.port = port;
    }

    @Value("${server.servlet.context-path}")
    public void setContentPath(String contentPath) {
        Application.contentPath = contentPath;
    }


}
