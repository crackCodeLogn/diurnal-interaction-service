package com.vv.personal.diurnal.ping.feign;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Vivek
 * @since 07/12/20
 */
public interface HealthFeign {

    @GetMapping("/health/ping")
    String ping();
}
