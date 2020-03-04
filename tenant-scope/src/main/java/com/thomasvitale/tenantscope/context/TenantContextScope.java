package com.thomasvitale.tenantscope.context;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TenantContextScope implements Scope {

    public static final String SCOPE_TENANT = "tenant";

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        TenantAttributes attributes = TenantContextHolder.currentTenantAttributes();
        Object scopedObject = attributes.getAttribute(name);
        if (scopedObject == null) {
            scopedObject = objectFactory.getObject();
            attributes.setAttribute(name, scopedObject);
        }
        return scopedObject;
    }

    @Override
    @Nullable
    public Object remove(String name) {
        TenantAttributes attributes = TenantContextHolder.currentTenantAttributes();
        Object scopedObject = attributes.getAttribute(name);
        if (scopedObject != null) {
            attributes.removeAttribute(name);
            return scopedObject;
        }
        else {
            return null;
        }
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(callback, "Callback must not be null");
        TenantAttributes attributes = TenantContextHolder.currentTenantAttributes();
        attributes.registerDestructionCallback(name, callback);
    }

    @Override
    @Nullable
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    @Nullable
    public String getConversationId() {
        TenantAttributes attributes = TenantContextHolder.currentTenantAttributes();
        return attributes.getTenant();
    }
}
