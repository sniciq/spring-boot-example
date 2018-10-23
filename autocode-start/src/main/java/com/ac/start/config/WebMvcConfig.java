package com.ac.start.config;

import com.ac.start.filter.AppInterceptor;
import com.ac.start.filter.TestFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.EventListener;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean testFilter() {
        FilterRegistrationBean register = new FilterRegistrationBean();
        register.setFilter(new TestFilter());
        register.addUrlPatterns("/test");
        register.setName("TestFilter");
        register.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return register;
    }

    @Bean
    public ServletListenerRegistrationBean<EventListener> getListener() {
        ServletListenerRegistrationBean<EventListener> rb = new ServletListenerRegistrationBean();
        rb.setListener(new AppServletContextListener());
        return rb;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public AppInterceptor appInterceptor() {
        return new AppInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**", "/directive/**", "/app/**", "/modules/**")
                .addResourceLocations("classpath:/static/", "classpath:/static/directive/", "classpath:/static/app/", "classpath:/static/modules/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for(HttpMessageConverter<?> converter : converters) {
            if(converter instanceof AbstractJackson2HttpMessageConverter) {
                AbstractJackson2HttpMessageConverter ja = (AbstractJackson2HttpMessageConverter) converter;
                ja.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            }
        }
    }
}
