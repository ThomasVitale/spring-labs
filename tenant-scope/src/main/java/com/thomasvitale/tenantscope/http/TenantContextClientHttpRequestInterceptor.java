package com.thomasvitale.tenantscope.http;

import com.thomasvitale.tenantscope.context.TenantContextHolder;
import com.thomasvitale.tenantscope.identification.MultitenancyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class TenantContextClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final MultitenancyProperties multitenancyProperties;

    @Autowired
    public TenantContextClientHttpRequestInterceptor(MultitenancyProperties multitenancyProperties) {
        this.multitenancyProperties = multitenancyProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String tenantId = TenantContextHolder.getTenantIdentifier();
        if (StringUtils.hasText(tenantId)) {
            request.getHeaders().add(multitenancyProperties.getHttpHeader(), tenantId);
        }
        return execution.execute(request, body);
    }
}
