package com.thomasvitale.tenantscope.camel;

import com.thomasvitale.tenantscope.context.TenantAttributes;
import com.thomasvitale.tenantscope.context.TenantContextHolder;
import com.thomasvitale.tenantscope.identification.TenantIdentificationResolver;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TenantAwareRouteBuilder extends RouteBuilder {

    private final TenantIdentificationResolver tenantIdentificationResolver;

    @Autowired
    public TenantAwareRouteBuilder(TenantIdentificationResolver tenantIdentificationResolver) {
        this.tenantIdentificationResolver = tenantIdentificationResolver;
    }

    @Override
    public void configure() throws Exception {
        interceptFrom()
                .process(exchange -> {
                    String tenantId = tenantIdentificationResolver.resolveTenant(exchange);
                    TenantAttributes tenantAttributes = new TenantAttributes(tenantId);
                    TenantContextHolder.setTenantAttributes(tenantAttributes);
                });

        //interceptSendToEndpoint("jpa:*").process(doHibernateMagic());
    }
}
