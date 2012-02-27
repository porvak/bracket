package com.porvak.bracket.config;

import com.porvak.bracket.socialize.signin.AccountExposingHandlerInterceptor;
import org.joda.time.DateTimeZone;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.inject.Inject;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.porvak.bracket", useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})

public class WebConfig extends WebMvcConfigurerAdapter {

    @Inject
    private Environment environment;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccountExposingHandlerInterceptor());
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setUseCacheControlNoStore(true);
        webContentInterceptor.setUseCacheControlHeader(true);
        webContentInterceptor.setUseExpiresHeader(true);
        webContentInterceptor.setCacheSeconds(0);
        registry.addInterceptor(webContentInterceptor);
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new DateTimeZoneHandlerMethodArgumentResolver());
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/").setCachePeriod(0);
    }

    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJacksonHttpMessageConverter());
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * Messages to support internationalization/localization.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/WEB-INF/messages/messages");
        if (environment.acceptsProfiles("embedded")) {
            messageSource.setCacheSeconds(0);
        }
        return messageSource;
    }


    // custom argument resolver inner classes
    private static class DateTimeZoneHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

        public boolean supportsParameter(MethodParameter parameter) {
            return DateTimeZone.class.isAssignableFrom(parameter.getParameterType());
        }

        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest,
                                      WebDataBinderFactory binderFactory) throws Exception {
            return JodaTimeContextHolder.getJodaTimeContext().getTimeZone();
        }

    }

}