package de.adorsys.smartanalytics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"de.adorsys.smartanalytics.pers"})
public class SmartanalyticsMongoConfiguration {
}
