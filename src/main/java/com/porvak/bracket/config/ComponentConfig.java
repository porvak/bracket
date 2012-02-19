package com.porvak.bracket.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Configuration
@ComponentScan(basePackages="com.porvak.bracket", useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class)})
public class ComponentConfig {

}
