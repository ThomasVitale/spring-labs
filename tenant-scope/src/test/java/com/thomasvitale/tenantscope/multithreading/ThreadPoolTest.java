package com.thomasvitale.tenantscope.multithreading;

import com.thomasvitale.tenantscope.TenantScopeApplication;
import com.thomasvitale.tenantscope.context.TenantAttributes;
import com.thomasvitale.tenantscope.context.TenantContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;

@SpringBootTest(classes = TenantScopeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThreadPoolTest {

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private TaskExecutor taskExecutor;

    @Test
    public void printMessages() {
        TenantAttributes tenantAttributes = new TenantAttributes("TENANT_ARGUS");
        TenantContextHolder.setTenantAttributes(tenantAttributes);
        for(int i = 0; i < 25; i++) {
            taskExecutor.execute(new MessagePrinterTask("Message" + i));
        }
    }

    private static class MessagePrinterTask implements Runnable {

        private String message;

        public MessagePrinterTask(String message) {
            this.message = message;
        }

        public void run() {
            System.out.println(message + " " + TenantContextHolder.getTenantIdentifier() + " " + Thread.currentThread().getName());
        }
    }
}
