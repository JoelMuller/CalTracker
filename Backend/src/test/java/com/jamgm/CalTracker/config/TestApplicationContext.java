package com.jamgm.CalTracker.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ComponentScan(basePackages = "com.jamgm.CalTracker")
@PropertySource("application-test.properties")
public class TestApplicationContext {
}
