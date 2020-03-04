package com.thomasvitale.tenantscope.config;

import com.thomasvitale.tenantscope.context.TenantAttributes;
import com.thomasvitale.tenantscope.context.TenantContextHolder;
import com.thomasvitale.tenantscope.context.TenantContextScope;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TenantConfig {

    @Bean
    public static CustomScopeConfigurer tenantScope() {
        CustomScopeConfigurer scopeConfigurer = new CustomScopeConfigurer();
        scopeConfigurer.addScope(TenantContextScope.SCOPE_TENANT, new TenantContextScope());
        return scopeConfigurer;
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        return new TaskExecutorBuilder()
                .corePoolSize(8)
                .maxPoolSize(16)
                .queueCapacity(25)
                .threadNamePrefix("tenant_task_executor_thread")
                .taskDecorator(runnable -> {
                    String tenantId = TenantContextHolder.getTenantIdentifier();
                    return () -> {
                        try {
                            TenantAttributes tenantAttributes = new TenantAttributes(tenantId);
                            TenantContextHolder.setTenantAttributes(tenantAttributes);
                            runnable.run();
                        } finally {
                            TenantContextHolder.resetTenantAttributes();
                        }
                    };
                })
                .build();
    }
}
