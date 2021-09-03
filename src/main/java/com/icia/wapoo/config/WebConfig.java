package com.icia.wapoo.config;

import com.icia.wapoo.WapooApplication;
import com.icia.wapoo.jwt.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = WapooApplication.class)
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    private static final String[] EXCLUDE_PATHS = {
            "/api/member/register",
            "/api/member/login",
            "/api/validation/**",
            "/store/getStoreListByLocation",
            "/store/getStoreFiles",
            "/store/getStoreInfos",
            "/order/**",
            "/**",
            "/review/**",
            "/article/**"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("제거된 경로: ");
        Arrays.stream(EXCLUDE_PATHS).forEach(System.out::println);
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Authorization");
    }
}
