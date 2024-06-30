package com.awd.feature.toggle.services.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({"com.awd.feature.toggle.services"})
@EnableJpaRepositories(basePackages = "com.awd.feature.toggle.services.dao.repository")
@Slf4j
public class FeatureToggleServiceConfiguration {
}
