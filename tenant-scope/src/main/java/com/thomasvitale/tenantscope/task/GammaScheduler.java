package com.thomasvitale.tenantscope.task;

import com.thomasvitale.tenantscope.context.TenantAttributes;
import com.thomasvitale.tenantscope.context.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GammaScheduler {

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private TaskExecutor taskExecutor;

    private static final String[] tenants = {"TENANT_ACME", "TENANT_ARGUS"};

    @Scheduled(initialDelay=1000, fixedRate=5000)
    public void doSomething() {
        Arrays.asList(tenants).forEach(tenant -> {
            TenantAttributes tenantAttributes = new TenantAttributes(tenant);
            TenantContextHolder.setTenantAttributes(tenantAttributes);
            taskExecutor.execute(() -> System.out.println("Scheduled job - " + TenantContextHolder.getTenantIdentifier() + " " + Thread.currentThread().getName()));
            TenantContextHolder.resetTenantAttributes();
        });
    }
}
