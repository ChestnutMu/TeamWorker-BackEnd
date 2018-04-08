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

//    @Bean
//    PermissionAuthentication getPermissionIntercepter() {
//        return new PermissionAuthentication();
//    }

    public void addInterceptors(InterceptorRegistry registry) {
        //-----------token认证-----------
        InterceptorRegistration addInterceptor = registry.addInterceptor(getTokenInterceptor());
        //不用token认证
        addInterceptor.excludePathPatterns("/user/login");
        addInterceptor.excludePathPatterns("/user/addUser");
        addInterceptor.excludePathPatterns("/user/getAllUsers");

        addInterceptor.excludePathPatterns("/department/getDepartments");
        addInterceptor.excludePathPatterns("/department/addDepartmentMemberRelation");

        addInterceptor.addPathPatterns("/**");


//        //------------权限认证------------
//        InterceptorRegistration addPermissionInterceptor = registry.addInterceptor(getPermissionIntercepter());
//
//        //部门权限
//        addPermissionInterceptor.addPathPatterns("/department/*");
//        addPermissionInterceptor.excludePathPatterns("/department/getDepartmentByUserId");
//        addPermissionInterceptor.excludePathPatterns("/department/buildTeam");
//
//        //考勤权限
//        addPermissionInterceptor.addPathPatterns("/attendance/*");
//        addPermissionInterceptor.excludePathPatterns("/attendance/getAttendance");
//        addPermissionInterceptor.excludePathPatterns("/attendance/punchIn");
//        addPermissionInterceptor.excludePathPatterns("/attendance/punchOut");
//
//        //成员权限管理的权限
//        addPermissionInterceptor.addPathPatterns("/permission/*");
//
//
//        //消息、通知、公告权限
//        // TODO: 2018/4/8 添加通知、公告的权限

        super.addInterceptors(registry);
    }
}
