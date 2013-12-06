package com.porvak.bracket.config;

import com.google.common.collect.Sets;
import com.porvak.bracket.util.PrincipalToAccountConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Configuration
@ComponentScan(basePackages="com.porvak.bracket", useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class)})
public class ComponentConfig {

    @Configuration
    @PropertySource("classpath:com/porvak/bracket/config/application.properties")
    @Profile("embedded")
    static class Embedded {
    }

    @Configuration
    @PropertySource("file:${cloud.config}/application.properties")
    @Profile("cloud")
    static class Cloud {
    }

    @Bean(name = "bracketConversionService")
    //TODO: Make this more spring 3.1 like.
    public ConversionService bracketConverters(){
        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(Sets.newHashSet(
                new PrincipalToAccountConverter()
        ));
        conversionServiceFactoryBean.afterPropertiesSet();
        return conversionServiceFactoryBean.getObject();
    }
}
