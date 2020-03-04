package com.thomasvitale.tenantscope.identification;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
class ExchangeTenantIdentificationStrategy implements TenantIdentificationStrategy {

    @Override
    public String getTenant(Object object) {
        Assert.isTrue(support(object), "Object must be of type Exchange");
        Exchange exchange = (Exchange) object;
        return (String) exchange.getProperty("tenant");
    }

    @Override
    public boolean support(Object object) {
        return object instanceof Exchange;
    }
}
