package de.adorsys.smartanalytics.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "de.adorsys.smartanalytics",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "de\\.adorsys\\.smartanalytics\\.config\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "de\\.adorsys\\.smartanalytics\\.exception\\..*"),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "de\\.adorsys\\.smartanalytics\\.web\\.ServerInfoController.*")
        }
)
public class SmartanalyticsConfiguration {
}
