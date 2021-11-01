package com.vv.personal.diurnal.interaction;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Observes;

@Slf4j
@QuarkusMain
public class DiurnalInteractionServer {
    private static final String HEROKU_SWAGGER_UI_URL = "https://%s/swagger-ui/index.html";
    private static final String SWAGGER_UI_URL = "http://%s:%s/swagger-ui/index.html";
    private static final String LOCALHOST = "localhost";
    private static final String LOCAL_SPRING_PORT = "local.server.port";
    private static final String SPRING_APPLICATION_HEROKU = "spring.application.heroku";

    public static void main(String[] args) {
        Quarkus.run(args);
    }

    void onStart(@Observes StartupEvent startupEvent) {
        log.info("********* Starting *********");
/*        String host = LOCALHOST;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Failed to obtain ip address. ", e);
        }
        String port = environment.getProperty(LOCAL_SPRING_PORT);
        String herokuHost = environment.getProperty(SPRING_APPLICATION_HEROKU);
        log.info("'{}' activation is complete! Expected Heroku Swagger running on: {}, exact url: {}",
                environment.getProperty("spring.application.name"),
                String.format(HEROKU_SWAGGER_UI_URL, herokuHost),
                String.format(SWAGGER_UI_URL, host, port));*/
    }
}