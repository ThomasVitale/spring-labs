package com.thomasvitale.tenantscope.context;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Scope(value = TenantContextScope.SCOPE_TENANT, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface TenantScope {
}
