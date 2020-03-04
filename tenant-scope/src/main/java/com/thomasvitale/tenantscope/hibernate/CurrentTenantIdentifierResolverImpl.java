package com.thomasvitale.tenantscope.hibernate;

import com.thomasvitale.tenantscope.context.TenantContextHolder;
import com.thomasvitale.tenantscope.identification.MultitenancyProperties;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Provides Hibernate with a strategy to resolve the tenant schema.
 */
@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private final MultitenancyProperties multitenancyProperties;

    @Autowired
    public CurrentTenantIdentifierResolverImpl(MultitenancyProperties multitenancyProperties) {
        this.multitenancyProperties = multitenancyProperties;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        return Objects.requireNonNullElse(TenantContextHolder.getTenantIdentifier(), multitenancyProperties.getDefaultTenantId());
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
