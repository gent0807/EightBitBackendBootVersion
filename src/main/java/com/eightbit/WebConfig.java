package com.eightbit;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    /*

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound","true");
        MultipartConfigElement multipartConfig=new MultipartConfigElement("C:\\upload\\temp", 20971520, 41943040, 20971520);
        registration.setMultipartConfig(multipartConfig);
    }

     */

    /*

    @Override
    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        return super.registerServletFilter(servletContext, filter);
    }

     */
    /*
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic filter= servletContext.addFilter("MultipartFilter", MultipartFilter.class);
        filter.addMappingForUrlPatterns(null,false,"/*");
        super.onStartup(servletContext);
    }
     */
    /*
    @Override
    public Filter[] getServletFilters(){
        return new Filter[] {new MultipartFilter()};
    }
    */
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
