package com.porvak.bracket.config;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Configuration
@ComponentScan(basePackages="com.porvak.bracket", useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class)})
public class ComponentConfig {

    @Configuration
    @PropertySource("classpath:com/porvak/bracket/config/application.properties")
    static class Standard {
    }
}
