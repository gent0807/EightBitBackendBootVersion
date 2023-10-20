package com.eightbit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc  // <annotation-driven/>
@ComponentScan
public class ServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry){
        InternalResourceViewResolver bean=new InternalResourceViewResolver();
        bean.setViewClass(JstlView.class);
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".jsp");
        registry.viewResolver(bean);
    }

    @Bean(name="multipartResolver")
    public CommonsMultipartResolver getResolver() throws Exception {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        //10MB
        resolver.setMaxUploadSize(1024 * 1024 * 10);
        //2MB
        resolver.setMaxUploadSizePerFile(1024 * 1024 * 2);
        //1MB
        resolver.setMaxInMemorySize(1024 * 1024);

        //temp upload
        resolver.setUploadTempDir(new FileSystemResource("C:\\upload\\temp"));
        resolver.setDefaultEncoding("utf-8");


        return resolver;
    }



}
