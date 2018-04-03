package com.info.xiaotingtingBackEnd.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Bean
    IdAuthentication getTokenInterceptor() {
        return new IdAuthentication();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getTokenInterceptor());
        //不用token认证
        addInterceptor.excludePathPatterns("/user/login");
        addInterceptor.excludePathPatterns("/user/addUser");
        addInterceptor.excludePathPatterns("/user/getAllUsers");

        addInterceptor.excludePathPatterns("/department/getDepartments");
        addInterceptor.excludePathPatterns("/department/addDepartmentRelation");

        addInterceptor.addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
