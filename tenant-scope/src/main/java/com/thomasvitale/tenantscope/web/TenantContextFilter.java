package com.thomasvitale.tenantscope.web;

import com.thomasvitale.tenantscope.context.TenantAttributes;
import com.thomasvitale.tenantscope.context.TenantContextHolder;
import com.thomasvitale.tenantscope.identification.TenantIdentificationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TenantContextFilter extends OncePerRequestFilter {

    private final TenantIdentificationResolver tenantIdentificationResolver;

    @Autowired
    public TenantContextFilter(TenantIdentificationResolver tenantIdentificationResolver) {
        this.tenantIdentificationResolver = tenantIdentificationResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String tenantId = tenantIdentificationResolver.resolveTenant(request);
            TenantAttributes tenantAttributes = new TenantAttributes(tenantId);
            TenantContextHolder.setTenantAttributes(tenantAttributes);
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.resetTenantAttributes();
        }
    }
}
