package com.thomasvitale.tenantscope.identification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

@Component
class RequestHeaderTenantIdentificationStrategy implements TenantIdentificationStrategy {

    private final MultitenancyProperties multitenancyProperties;

    @Autowired
    public RequestHeaderTenantIdentificationStrategy(MultitenancyProperties multitenancyProperties) {
        this.multitenancyProperties = multitenancyProperties;
    }

    @Override
    public String getTenant(Object object) {
        Assert.isTrue(support(object), "Object must be of type HttpServletRequest");
        HttpServletRequest request = (HttpServletRequest) object;
        return request.getHeader(multitenancyProperties.getHttpHeader());
    }

    @Override
    public boolean support(Object object) {
        return object instanceof HttpServletRequest;
    }
}
