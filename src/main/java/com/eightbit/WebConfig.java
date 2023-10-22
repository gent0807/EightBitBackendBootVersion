package com.eightbit;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

@Configuration
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

//    @Bean
//    public FilterRegistrationBean someFilterRegistration() {
//
//      FilterRegistrationBean registration = new FilterRegistrationBean();
//      registration.setFilter(someFilter());
//      registration.addUrlPatterns("/*");
//      registration.addInitParameter("paramName", "paramValue");
//      registration.setName("Multipart Filter");
//      registration.setOrder(1);
//      return registration;
//    }

//   public Filter someFilter() {
//        return new MultipartFilter();
//   }

    @Override
    protected Class<?>[] getRootConfigClasses(){
        return  new Class[] {RootConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses(){
        return new Class[] {ServletConfig.class};
    }

    @Override
    protected  String[] getServletMappings(){
        return new String[] {"/"};
    }
}
