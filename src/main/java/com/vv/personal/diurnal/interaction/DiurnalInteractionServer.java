package com.vv.personal.diurnal.interaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.vv.personal.diurnal.interaction.constants.Constants.*;

@ComponentScan({"com.vv.personal.diurnal.interaction", "com.vv.personal.diurnal.ping"})
@SpringBootApplication
public class DiurnalInteractionServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiurnalInteractionServer.class);

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(DiurnalInteractionServer.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vv.personal.diurnal"))
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void firedUpAllCylinders() {
        String host = LOCALHOST;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("Failed to obtain ip address. ", e);
        }
        String port = environment.getProperty(LOCAL_SPRING_PORT);
        String herokuHost = environment.getProperty(SPRING_APPLICATION_HEROKU);
        LOGGER.info("'{}' activation is complete! Expected Heroku Swagger running on: {}, exact url: {}",
                environment.getProperty("spring.application.name"),
                String.format(HEROKU_SWAGGER_UI_URL, herokuHost),
                String.format(SWAGGER_UI_URL, host, port));
    }

}
