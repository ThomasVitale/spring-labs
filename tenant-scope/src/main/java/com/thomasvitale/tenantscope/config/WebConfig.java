package com.thomasvitale.tenantscope.config;

import com.thomasvitale.tenantscope.web.TenantContextHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TenantContextHandlerInterceptor tenantContextHandlerInterceptor;

    @Autowired
    public WebConfig(TenantContextHandlerInterceptor tenantContextHandlerInterceptor) {
        this.tenantContextHandlerInterceptor = tenantContextHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantContextHandlerInterceptor);
    }
}
