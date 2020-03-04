package com.thomasvitale.tenantscope.identification;

/**
 * Abstraction of a strategy to identify the tenant target of a specific task.
 */
public interface TenantIdentificationStrategy {

    /**
     * Determine the tenant based on the given object.
     * If the strategy cannot determine a tenant, return {@code null}.
     *
     * @param object the object used to identify the tenant.
     * @return the tenant or {@code null}.
     */
    String getTenant(Object object);

    /**
     * Verifies if the given object is supported by the current strategy.
     * @param object the object used to identify the tenant.
     * @return true if the object is supported by the strategy, false otherwise.
     */
    boolean support(Object object);
}
