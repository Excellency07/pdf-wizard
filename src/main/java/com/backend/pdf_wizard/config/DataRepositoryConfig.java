package com.backend.pdf_wizard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.backend.pdf_wizard.dao.repository.jpa")
@EnableRedisRepositories(basePackages = "com.backend.pdf_wizard.dao.repository.redis")
public class DataRepositoryConfig {
}
