package com.thomasvitale.tenantscope.identification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantIdentificationResolver {

    private final List<TenantIdentificationStrategy> strategies;
    private final MultitenancyProperties multitenancyProperties;

    @Autowired
    public TenantIdentificationResolver(List<TenantIdentificationStrategy> strategies, MultitenancyProperties multitenancyProperties) {
        this.strategies = strategies;
        this.multitenancyProperties = multitenancyProperties;
    }

    public String resolveTenant(Object object) {
        for (TenantIdentificationStrategy strategy : this.strategies) {
            if (strategy.support(object)) {
                return strategy.getTenant(object);
            }
        }

        return multitenancyProperties.getDefaultTenantId();
    }
}
